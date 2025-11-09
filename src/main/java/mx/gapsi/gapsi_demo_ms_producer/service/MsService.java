package mx.gapsi.gapsi_demo_ms_producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.model.Label;
import mx.gapsi.gapsi_demo_ms_producer.dao.MsDao;

@Service
public class MsService {

    @Autowired
    private MsDao msDao;

    KafkaTemplate<String, Label> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topicKafka;

    public MsService(KafkaTemplate<String, Label> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Base findAll (Base base) {
        try {
            msDao.findAll(base);
            createCustomerKafka(base.getCustomDto().getData().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base;
    }

    @SuppressWarnings("null")
    public void createCustomerKafka(Object objectLabel) throws Exception {
        Label label = (Label) objectLabel;
        SendResult<String, Label> result =
                kafkaTemplate.send(topicKafka, label.getLabelId().toString(), label).get();
        System.out.println("partition() " + result.getRecordMetadata().partition());
        System.out.println("topic() " + result.getRecordMetadata().topic());
        System.out.println("offset() " + result.getRecordMetadata().offset());
    }
    
}
