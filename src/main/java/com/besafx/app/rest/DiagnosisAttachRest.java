package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Attach;
import com.besafx.app.entity.DiagnosisAttach;
import com.besafx.app.service.AttachService;
import com.besafx.app.service.DiagnosisAttachService;
import com.besafx.app.service.DiagnosisService;
import com.besafx.app.service.PersonService;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/api/diagnosisAttach/")
public class DiagnosisAttachRest {

    public static final String FILTER_TABLE = "id,diagnosis[id],attach[**,person[id,nickname,name]]";
    private final static Logger log = LoggerFactory.getLogger(DiagnosisAttachRest.class);
    @Autowired
    private PersonService personService;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private DiagnosisAttachService diagnosisAttachService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private DropboxManager dropboxManager;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DIAGNOSIS_ATTACH_CREATE')")
    public String upload(@RequestParam(value = "diagnosisId") Long diagnosisId,
                         @RequestParam(value = "fileName") String fileName,
                         @RequestParam(value = "mimeType") String mimeType,
                         @RequestParam(value = "description") String description,
                         @RequestParam(value = "remote") Boolean remote,
                         @RequestParam(value = "file") MultipartFile file,
                         Principal principal)
            throws ExecutionException, InterruptedException, IOException {

        DiagnosisAttach diagnosisAttach = new DiagnosisAttach();
        diagnosisAttach.setDiagnosis(diagnosisService.findOne(diagnosisId));

        Attach attach = new Attach();
        attach.setName(fileName);
        attach.setMimeType(mimeType);
        attach.setDescription(description);
        attach.setSize(file.getSize());
        attach.setDate(new DateTime().toDate());
        attach.setRemote(remote);
        attach.setPerson(personService.findByEmail(principal.getName()));

        String path = "/Pharmacy4Falcon/Diagnoses/" + diagnosisId + "/" + fileName + "." + mimeType;

        Future<Boolean> uploadTask = dropboxManager.uploadFile(file, path);
        if (uploadTask.get()) {
            Future<String> shareTask = dropboxManager.shareFile(path);
            attach.setLink(shareTask.get());
            attach = attachService.save(attach);
            diagnosisAttach.setAttach(attach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("طلبات الفحص")
                    .message("تم رفع الملف" + " [ " + file.getOriginalFilename() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-upload")
                    .build(), principal.getName());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), diagnosisAttachService.save(diagnosisAttach));
        }else{
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DIAGNOSIS_ATTACH_DELETE')")
    public Boolean delete(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        DiagnosisAttach diagnosisAttach = diagnosisAttachService.findOne(id);
        if (diagnosisAttach != null) {
            Future<Boolean> deleteTask = dropboxManager.deleteFile("/Pharmacy4Falcon/Diagnoses/" + diagnosisAttach.getDiagnosis().getId() + "/" + diagnosisAttach.getAttach().getName() + "." + diagnosisAttach.getAttach().getMimeType());
            if (deleteTask.get()) {
                diagnosisAttachService.delete(diagnosisAttach);
                notificationService.notifyOne(Notification
                        .builder()
                        .title("العيادة الطبية")
                        .message("تم حذف الملف" + " [ " + diagnosisAttach.getAttach().getName() + " ] " + " بنجاح.")
                        .type("success")
                        .icon("fa-trash")
                        .build(), principal.getName());
                return true;
            } else {
                notificationService.notifyOne(Notification
                        .builder()
                        .title("العيادة الطبية")
                        .message("لا يمكن حذف الملف" + " [ " + diagnosisAttach.getAttach().getName() + " ] ")
                        .type("error")
                        .icon("fa-trash")
                        .build(), principal.getName());
                return false;
            }
        }else{
            return false;
        }
    }

    @RequestMapping(value = "deleteWhatever/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DIAGNOSIS_ATTACH_DELETE')")
    public void deleteWhatever(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        DiagnosisAttach diagnosisAttach = diagnosisAttachService.findOne(id);
        if (diagnosisAttach != null) {
            diagnosisAttachService.delete(diagnosisAttach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("العيادة الطبية")
                    .message("تم حذف المرفق" + " [ " + diagnosisAttach.getAttach().getName() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-trash")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByDiagnosis/{diagnosisId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDiagnosis(@PathVariable(value = "diagnosisId") Long diagnosisId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), diagnosisAttachService.findByDiagnosisId(diagnosisId));
    }
}
