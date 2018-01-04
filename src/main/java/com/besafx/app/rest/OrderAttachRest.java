package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Attach;
import com.besafx.app.entity.OrderAttach;
import com.besafx.app.service.AttachService;
import com.besafx.app.service.OrderAttachService;
import com.besafx.app.service.OrderService;
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
@RequestMapping(value = "/api/orderAttach/")
public class OrderAttachRest {

    public static final String FILTER_TABLE = "id,order[id],attach[**,person[id,nickname,name]]";
    private final static Logger log = LoggerFactory.getLogger(OrderAttachRest.class);
    @Autowired
    private PersonService personService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAttachService orderAttachService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private DropboxManager dropboxManager;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_ATTACH_CREATE')")
    public String upload(@RequestParam(value = "orderId") Long orderId,
                         @RequestParam(value = "fileName") String fileName,
                         @RequestParam(value = "mimeType") String mimeType,
                         @RequestParam(value = "description") String description,
                         @RequestParam(value = "remote") Boolean remote,
                         @RequestParam(value = "file") MultipartFile file,
                         Principal principal)
            throws ExecutionException, InterruptedException, IOException {

        OrderAttach orderAttach = new OrderAttach();
        orderAttach.setOrder(orderService.findOne(orderId));

        Attach attach = new Attach();
        attach.setName(fileName);
        attach.setMimeType(mimeType);
        attach.setDescription(description);
        attach.setSize(file.getSize());
        attach.setDate(new DateTime().toDate());
        attach.setRemote(remote);
        attach.setPerson(personService.findByEmail(principal.getName()));

        String path = "/Pharmacy4Falcon/Orders/" + orderId + "/" + fileName + "." + mimeType;

        Future<Boolean> uploadTask = dropboxManager.uploadFile(file, path);
        if (uploadTask.get()) {
            Future<String> shareTask = dropboxManager.shareFile(path);
            attach.setLink(shareTask.get());
            attach = attachService.save(attach);
            orderAttach.setAttach(attach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("طلبات الفحص")
                    .message("تم رفع الملف" + " [ " + file.getOriginalFilename() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-upload")
                    .build(), principal.getName());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderAttachService.save(orderAttach));
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_ATTACH_DELETE')")
    public Boolean delete(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        OrderAttach orderAttach = orderAttachService.findOne(id);
        if (orderAttach != null) {
            Future<Boolean> deleteTask = dropboxManager.deleteFile("/Pharmacy4Falcon/Orders/" + orderAttach.getOrder().getId() + "/" + orderAttach.getAttach().getName() + "." + orderAttach.getAttach().getMimeType());
            if (deleteTask.get()) {
                orderAttachService.delete(orderAttach);
                notificationService.notifyOne(Notification
                        .builder()
                        .title("طلبات الفحص")
                        .message("تم حذف الملف" + " [ " + orderAttach.getAttach().getName() + " ] " + " بنجاح.")
                        .type("success")
                        .icon("fa-trash")
                        .build(), principal.getName());
                return true;
            } else {
                notificationService.notifyOne(Notification
                        .builder()
                        .title("طلبات الفحص")
                        .message("لا يمكن حذف الملف" + " [ " + orderAttach.getAttach().getName() + " ] ")
                        .type("error")
                        .icon("fa-trash")
                        .build(), principal.getName());
                return false;
            }
        } else {
            return false;
        }
    }

    @RequestMapping(value = "deleteWhatever/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_ATTACH_DELETE')")
    public void deleteWhatever(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        OrderAttach orderAttach = orderAttachService.findOne(id);
        if (orderAttach != null) {
            orderAttachService.delete(orderAttach);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("طلبات الفحص")
                    .message("تم حذف المرفق" + " [ " + orderAttach.getAttach().getName() + " ] " + " بنجاح.")
                    .type("success")
                    .icon("fa-trash")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByOrder/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrder(@PathVariable(value = "orderId") Long orderId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderAttachService.findByOrderId(orderId));
    }
}
