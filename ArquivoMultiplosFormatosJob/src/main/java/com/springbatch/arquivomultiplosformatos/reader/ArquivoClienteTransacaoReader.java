package com.springbatch.arquivomultiplosformatos.reader;

import com.springbatch.arquivomultiplosformatos.dominio.Cliente;
import com.springbatch.arquivomultiplosformatos.dominio.Transacao;
import org.springframework.batch.item.*;

public class ArquivoClienteTransacaoReader implements ItemStreamReader<Cliente> {

    private Object objetoAtual;
    private ItemStreamReader<Object> delegate;

    public ArquivoClienteTransacaoReader(ItemStreamReader<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Cliente read() throws Exception {
        if (objetoAtual == null) {
            objetoAtual = delegate.read();
        }

        Cliente cliente = (Cliente) objetoAtual;
        objetoAtual = null;

        if (cliente != null) {
            while (peek() instanceof Transacao) {
                cliente.getTransacaoes().add((Transacao) objetoAtual);
            }
        }

        return cliente;
    }

    private Object peek() throws Exception {
        objetoAtual = delegate.read();
        return objetoAtual;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
