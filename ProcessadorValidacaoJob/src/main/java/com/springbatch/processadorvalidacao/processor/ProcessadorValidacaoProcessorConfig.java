package com.springbatch.processadorvalidacao.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springbatch.processadorvalidacao.dominio.Cliente;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ProcessadorValidacaoProcessorConfig {
    private Set<String> emails = new HashSet<String>();

    @Bean
    public ItemProcessor<Cliente, Cliente> procesadorValidacaoProcessor() throws Exception {
        return new CompositeItemProcessorBuilder<Cliente, Cliente>()
                .delegates(beanValidatingItemProcessor(), emailValidationProcessor())
                .build();
    }

    private BeanValidatingItemProcessor<Cliente> beanValidatingItemProcessor() throws Exception {
        BeanValidatingItemProcessor<Cliente> processor = new BeanValidatingItemProcessor();
        processor.setFilter(true);
        processor.afterPropertiesSet();
        return processor;
    }

    private ValidatingItemProcessor emailValidationProcessor() {
        ValidatingItemProcessor<Cliente> processor = new ValidatingItemProcessor<>();
        processor.setValidator(validator());
        processor.setFilter(true);
        return processor;

    }

    private Validator<? super Cliente> validator() {

        return new Validator<Cliente>() {
            @Override
            public void validate(Cliente cliente) throws ValidationException {
                if (emails.contains(cliente.getEmail())) {
                    throw new ValidationException("erro de duplicação");
                }else {
                    emails.add(cliente.getEmail());
                }
            }
        };
    }


}
