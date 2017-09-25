package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Attach;
import com.besafx.app.entity.OrderDetectionTypeAttach;
import com.besafx.app.service.AttachService;
import com.besafx.app.service.OrderDetectionTypeAttachService;
import com.besafx.app.service.OrderDetectionTypeService;
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
@RequestMapping(value = "/api/orderDetectionTypeAttach/")
public class OrderDetectionTypeAttachRest {

    public static final String FILTER_TABLE = "id,orderDetectionType[id],attach[**,person[id,nickname,name]]";
    private final static Logger log = LoggerFactory.getLogger(OrderDetectionTypeAttachRest.class);
    @Autowired
    private PersonService personService;

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    @Autowired
    private OrderDetectionTypeAttachService orderDetectionTypeAttachService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private DropboxManager dropboxManager;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DETECTION_TYPE_ATTACH_CREATE')")
    public String upload(@RequestParam(value = "orderDetectionTypeId") Long orderDetectionTypeId,
                         @RequestParam(value = "fileName") String fileName,
                         @RequestParam(value = "mimeType") String mimeType,
                         @RequestParam(value = "description") String description,
                         @RequestParam(value = "remote") Boolean remote,
                         @RequestParam(value = "file") MultipartFile file,
                         Principal principal)
            throws ExecutionException, InterruptedException, IOException {

        OrderDetectionTypeAttach orderDetectionTypeAttach = new OrderDetectionTypeAttach();
        orderDetectionTypeAttach.setOrderDetectionType(orderDetectionTypeService.findOne(orderDetectionTypeId));

        Attach attach = new Attach();
        attach.setName(fileName);
        attach.setMimeType(mimeType);
        attach.setDescription(description);
        attach.setSize(file.getSize());
        attach.setDate(new DateTime().toDate());
        attach.setRemote(remote);
        attach.setPerson(personService.findByEmail(principal.getName()));

        String path = "/Pharmacy4Falcon/OrderDetectionType/" + orderDetectionTypeId + "/" + fileName + "." + mimeType;

        Future<Boolean> uploadTask = dropboxManager.uploadFile(file, path);
        if (uploadTask.get()) {
            Future<String> shareTask = dropboxManager.shareFile(path);
            attach.setLink(shareTask.get());
            attach = attachService.save(attach);
            orderDetectionTypeAttach.setAttach(attach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("الاستقبال")
                    .message("تم رفع الملف" + " [ " + file.getOriginalFilename() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-upload")
                    .build(), principal.getName());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderDetectionTypeAttachService.save(orderDetectionTypeAttach));
        }else{
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DETECTION_TYPE_ATTACH_DELETE')")
    public Boolean delete(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        OrderDetectionTypeAttach orderDetectionTypeAttach = orderDetectionTypeAttachService.findOne(id);
        if (orderDetectionTypeAttach != null) {
            Future<Boolean> deleteTask = dropboxManager.deleteFile("/Pharmacy4Falcon/OrderDetectionType/" + orderDetectionTypeAttach.getOrderDetectionType().getId() + "/" + orderDetectionTypeAttach.getAttach().getName() + "." + orderDetectionTypeAttach.getAttach().getMimeType());
            if (deleteTask.get()) {
                orderDetectionTypeAttachService.delete(orderDetectionTypeAttach);
                notificationService.notifyOne(Notification
                        .builder()
                        .title("الاستقبال")
                        .message("تم حذف الملف" + " [ " + orderDetectionTypeAttach.getAttach().getName() + " ] " + " بنجاح.")
                        .type("success")
                        .icon("fa-trash")
                        .build(), principal.getName());
                return true;
            } else {
                notificationService.notifyOne(Notification
                        .builder()
                        .title("الاستقبال")
                        .message("لا يمكن حذف الملف" + " [ " + orderDetectionTypeAttach.getAttach().getName() + " ] ")
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
    @PreAuthorize("hasRole('ROLE_ORDER_DETECTION_TYPE_ATTACH_DELETE')")
    public void deleteWhatever(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        OrderDetectionTypeAttach orderDetectionTypeAttach = orderDetectionTypeAttachService.findOne(id);
        if (orderDetectionTypeAttach != null) {
            orderDetectionTypeAttachService.delete(orderDetectionTypeAttach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("الإستقبال")
                    .message("تم حذف المرفق" + " [ " + orderDetectionTypeAttach.getAttach().getName() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-trash")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByOrderDetectionType/{orderDetectionTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrderDetectionType(@PathVariable(value = "orderDetectionTypeId") Long orderDetectionTypeId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderDetectionTypeAttachService.findByOrderDetectionTypeId(orderDetectionTypeId));
    }
}
