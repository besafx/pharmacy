package com.besafx.app.entity;

import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.comparator.OrderDetectionTypeComparator;
import com.besafx.app.entity.enums.OrderCondition;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.OrderService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.catalina.core.ApplicationContext;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SortComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "[Order]")
@Component
public class Order implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Order.class);

    private static final long serialVersionUID = 1L;

    @Transient
    private static OrderService orderService;

    @PostConstruct
    public void init() {
        try{
            orderService = BeanUtil.getBean(OrderService.class);
        }catch (Exception ex){

        }
    }

    @GenericGenerator(
            name = "orderSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSequenceGenerator")
    private Long id;

    private Integer code;

    @Enumerated(EnumType.STRING)
    private OrderCondition orderCondition;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Integer checkCode;

    private Double discount;

    @ManyToOne
    @JoinColumn(name = "falcon")
    private Falcon falcon;

    @ManyToOne
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @SortComparator(OrderDetectionTypeComparator.class)
    private List<OrderDetectionType> orderDetectionTypes = new ArrayList<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderAttach> orderAttaches = new ArrayList<>();

    public OrderCondition getOrderCondition(){
        if(this.id == null){
            return this.orderCondition;
        }
        try{
            long numberOfUnCheckedOrderDetectionTypes = this.orderDetectionTypes
                    .stream()
                    .filter(orderDetectionType -> orderDetectionType.getDiagnoses().isEmpty())
                    .count();
            log.info("////////////////////////////////////////////////////////////////////////");
            log.info("فحص الطلب رقم / " + this.getCode());
            log.info("عدد خدمات الطلب الغير مفحوصة = " + numberOfUnCheckedOrderDetectionTypes);
            log.info("////////////////////////////////////////////////////////////////////////");
            if(this.orderDetectionTypes.isEmpty()){
                this.orderCondition = OrderCondition.Pending;
                orderService.save(this);
            }
            else if(numberOfUnCheckedOrderDetectionTypes > 0 && !orderCondition.equals(OrderCondition.Pending)){
                this.orderCondition = OrderCondition.Pending;
                orderService.save(this);
            }else if(numberOfUnCheckedOrderDetectionTypes == 0 && !this.orderDetectionTypes.isEmpty()){
                this.orderCondition = OrderCondition.Diagnosed;
                orderService.save(this);
            }
            return this.orderCondition;
        }catch (Exception ex){
            return this.orderCondition;
        }
    }

    public Double getDetectionTypeCostSum() {
        try {
            return this.orderDetectionTypes
                    .stream()
                    .map(OrderDetectionType::getDetectionType)
                    .mapToDouble(DetectionType::getCost)
                    .sum();
        } catch (Exception ex) {
            return null;
        }
    }

    public Double getNetCost() {
        try {
            Double total = getDetectionTypeCostSum();
            return total - (total * this.discount / 100);
        } catch (Exception ex) {
            return null;
        }
    }

    @JsonCreator
    public static Order Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonString, Order.class);
        return order;
    }
}
