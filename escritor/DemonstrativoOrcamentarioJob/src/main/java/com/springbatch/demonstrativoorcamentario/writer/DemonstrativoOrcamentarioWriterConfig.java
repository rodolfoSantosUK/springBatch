package com.springbatch.demonstrativoorcamentario.writer;

import com.springbatch.demonstrativoorcamentario.dominio.Lancamento;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.springbatch.demonstrativoorcamentario.dominio.GrupoLancamento;
import org.springframework.core.io.Resource;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

@Configuration
public class DemonstrativoOrcamentarioWriterConfig {

    @Bean
    @StepScope
    public MultiResourceItemWriter<GrupoLancamento> multiDemonstrativoOrcamentarioWriter(
            @Value("#{jobParameters['demonstrativosOrcamentarios']}") Resource demonstrativosOrcamentarios,
            FlatFileItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter
    ) {

        return new MultiResourceItemWriterBuilder<GrupoLancamento>()
                .name("multiDemonstrativoOrcamentarioWriter")
                .resource(demonstrativosOrcamentarios)
                .delegate(demonstrativoOrcamentarioWriter)
                .resourceSuffixCreator(suffixCreator())
                .itemCountLimitPerResource(1)
                .build();
    }

    private ResourceSuffixCreator suffixCreator() {
        return new ResourceSuffixCreator() {
            @Override
            public String getSuffix(int i) {
                return i + ".txt";
            }
        };
    }


    @StepScope
    @Bean
    public FlatFileItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter(
            @Value("#{jobParameters['demonstrativoOrcamentario']}") Resource demonstrativoOrcamentario
    ) {
        return new FlatFileItemWriterBuilder<GrupoLancamento>()
                .name("demonstrativoOrcamentarioWriter")
                .resource(demonstrativoOrcamentario)
                .lineAggregator(lineAggregator())
                .build();

    }

    private LineAggregator<GrupoLancamento> lineAggregator() {
        return new LineAggregator<GrupoLancamento>() {

            @Override
            public String aggregate(GrupoLancamento grupo) {
                String formatGrupoLancamento = String.format("[%d] %s - %s", grupo.getCodigoNaturezaDespesa(),
                        grupo.getDescricaoNaturezaDespesa(),
                        NumberFormat.getCurrencyInstance().format(grupo.getTotal()));
                StringBuilder strBuilder = new StringBuilder();

                for (Lancamento lancamento : grupo.getLancamentos()) {
                    strBuilder.append(String.format("\t [%s] %s - %s", new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getData()), lancamento.getDescricao(),
                            NumberFormat.getCurrencyInstance().format(lancamento.getValor())));
                }
                String formatLancamento = strBuilder.toString();
                return formatGrupoLancamento + formatLancamento;
            }
        };
    }

}


















