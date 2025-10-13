package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.*;
import com.example.Sistema_Gastos_Review.dto.response.*;
import com.example.Sistema_Gastos_Review.entity.*;
import com.example.Sistema_Gastos_Review.mapper.TransacaoMapper;
import com.example.Sistema_Gastos_Review.repository.*;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final CarteiraRepository carteiraRepository;
    private final TransacaoRepository transacaoRepository;
    private final DepositoRepository depositoRepository;
    private final SaqueRepository saqueRepository;
    private final ContaCarteiraRepository contaCarteiraRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final CriarCarteiraRepository criarCarteiraRepository;
    private final DeletarCarteiraRepository deletarCarteiraRepository;
    private final PagamentoBoletoRepository pagamentoBoletoRepository;
    private final PagamentoDebitoRepository pagamentoDebitoRepository;
    private final PixRepository pixRepository;

    public TransacaoService(
            UsuarioRepository usuarioRepository,
            ContaRepository contaRepository,
            CarteiraRepository carteiraRepository,
            TransacaoRepository transacaoRepository,
            DepositoRepository depositoRepository,
            SaqueRepository saqueRepository,
            ContaCarteiraRepository contaCarteiraRepository,
            TransferenciaRepository transferenciaRepository,
            CriarCarteiraRepository criarCarteiraRepository,
            DeletarCarteiraRepository deletarCarteiraRepository,
            PagamentoBoletoRepository pagamentoBoletoRepository,
            PagamentoDebitoRepository pagamentoDebitoRepository,
            PixRepository pixRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
        this.depositoRepository = depositoRepository;
        this.saqueRepository = saqueRepository;
        this.contaCarteiraRepository = contaCarteiraRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.criarCarteiraRepository = criarCarteiraRepository;
        this.deletarCarteiraRepository = deletarCarteiraRepository;
        this.pagamentoBoletoRepository = pagamentoBoletoRepository;
        this.pagamentoDebitoRepository = pagamentoDebitoRepository;
        this.pixRepository = pixRepository;
    }

    public BaseResponse criarSaqueConta(String idUsuario, String idConta, CriarSaqueContaRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Saque negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do Saque não é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do saque é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Saque saque = TransacaoMapper.toSaqueContaEntity(request, conta);

        //Mapper que tem o metodo de Saque da Conta
        TransacaoMapper.sacar(conta, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(saque);

        //Aqui eu salvo a conta com seu saldo atualizado
        contaRepository.save(conta);

        return new BaseResponse(
                "Saque realizado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueResponse(conta, saque));
    }

    public BaseResponse criarDepositoConta(String idUsuario, String idConta, CriarDepositoContaRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Deposito negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do saque é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Deposito deposito = TransacaoMapper.toDepositoContaEntity(request, conta);

        //Mapper que tem o metodo de Deposito da Conta
        TransacaoMapper.depositar(conta, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(deposito);

        //Aqui eu salvo a conta com seu saldo atualizado
        contaRepository.save(conta);

        return new BaseResponse(
                "Deposito efetuado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoResponse(conta, deposito));
    }

    public BaseResponse criarDepositoNaCarteira(String idUsuario, String idConta, String idCarteira, CriarDepositoNaCarteiraRequest request) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se o valor de deposito é valido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se o valor do deposito na carteira é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraDepositoEntity(conta, carteira, request);

        //Mapper que tem o metodo de Deposito da Carteira
        TransacaoMapper.depositarNaCarteira(contaCarteira);

        //Aqui eu salvo a transacao
        transacaoRepository.save(contaCarteira);

        //Aqui eu salvo a conta com o saldo atualizado
        contaRepository.save(conta);
        return new BaseResponse(

                "Deposito na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoNaCarteiraResponse(contaCarteira)
        );
    }

    public BaseResponse criarSaqueNaCarteira(String idUsuario, String idConta, String idCarteira, CriarSaqueNaCarteiraRequest request) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se o valor de saque é valido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se o valor de saque é maior que o saldo
        if (request.valor().compareTo(carteira.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraSaqueEntity(conta, carteira, request);

        //Mapper que tem o metodo de Saque da Carteira
        TransacaoMapper.sacarDaCarteira(contaCarteira);

        //Aqui eu salvo a transacao
        transacaoRepository.save(contaCarteira);

        //Aqui eu salvo a conta com o saldo atualizado
        contaRepository.save(conta);
        return new BaseResponse(
                "Saque na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueNaCarteiraResponse(contaCarteira)
        );
    }

    // A anotação @Transactional garante que todas as operações dentro do metodo
    // aconteçam de forma atômica — ou seja, se der erro no meio, tudo é revertido.
    @Transactional
    public BaseResponse criarTransferencia(String idUsuario, String idConta, CriarTransferenciaRequest request) {
        final int MAX_RETRIES = 3; // Define o número máximo de tentativas em caso de conflito (OptimisticLock)
        int attempts = 0;

        // Loop que tentará repetir a operação até o limite de tentativas
        while (attempts < MAX_RETRIES) {
            try {
                // Chama o metodo que executa de fato a lógica da transferência
                return executarTransferencia(idUsuario, idConta, request);
            } catch (OptimisticLockException e) {
                // Caso duas transações tentem alterar a mesma conta ao mesmo tempo,
                // essa exceção será lançada.
                attempts++;
                if (attempts == MAX_RETRIES) {
                    // Após atingir o número máximo de tentativas, retorna erro de conflito
                    return new BaseResponse(
                            "Falha na transferencia devido a concorrência. Tente novamente.",
                            HttpStatus.CONFLICT,
                            null
                    );
                }
                // Pequena pausa antes de tentar novamente (boa prática para aliviar concorrência)
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
        }

        // Caso ocorra algum erro inesperado fora do loop
        return new BaseResponse("Falha desconhecida na transferencia.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private BaseResponse executarTransferencia(String idUsuario, String idConta, CriarTransferenciaRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Transferencia negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Transferencia negada! Conta de origem nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Conta contaOrigem = contaEncontrada.get();

        if (!contaOrigem.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Transferencia negada! Conta não pertence ao usuário informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se o valor de transferencia é maior que o saldo
        if (request.valor().compareTo(contaOrigem.getSaldo()) > 0) {
            return new BaseResponse(
                    "Transferencia negada! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se a tranferência é para um banco externo
        if (!request.bancoDestino().equalsIgnoreCase("BCO AGIBANK S.A.")) {

            //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
            Transferencia transferenciaExterna = TransacaoMapper.toTransferenciaEntityExterna(contaOrigem, request.numeroContaDestino(), request);

            //Mapper que tem o metodo de Transferencia Externa
            TransacaoMapper.aplicarTransferenciaExterna(contaOrigem, request);

            //Aqui eu salvo a transacao
            transacaoRepository.save(transferenciaExterna);

            //Aqui eu salvo a conta com o saldo atualizado
            contaRepository.save(contaOrigem);
            return new BaseResponse("Transferencia realizada com sucesso!", HttpStatus.CREATED,
                    TransacaoMapper.toCriarTransferenciaResponse(transferenciaExterna));
        }

        Optional<Conta> contaDestinoEncontrada = contaRepository.findByNumero(request.numeroContaDestino());

        //Validação para saber se a conta destino é válida
        if (contaDestinoEncontrada.isEmpty()) {
            return new BaseResponse("Transferencia negada! Conta destino não encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        // Transferência interna
        Conta contaDestino = contaDestinoEncontrada.get();

        //Validação para saber se a conta de Origem está deletada
        if (contaOrigem.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Transferencia negada! Conta origem deletada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        //Validação para saber se a conta de Destino está deletada
        if (contaDestino.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Transferencia negada! Conta destino deletada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Transferencia pagamentoInterno = TransacaoMapper.toTransferenciaEntityInterna(contaOrigem, contaDestino, request);

        //Mapper que tem o metodo de Transferencia Interna
        TransacaoMapper.aplicarTransferenciaInterna(contaOrigem, contaDestino, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(pagamentoInterno);

        //Aqui eu salvo a conta de origem com o saldo atualizado
        contaRepository.save(contaOrigem);

        //Aqui eu salvo a conta de destino com o saldo atualizado
        contaRepository.save(contaDestino);

        return new BaseResponse("Tranferencia realizada com sucesso!", HttpStatus.CREATED,
                TransacaoMapper.toCriarTransferenciaResponse(pagamentoInterno));
    }

    public BaseResponse criarPagamentoBoleto(String idUsuario, String idConta, CriarPagamentoBoletoRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do pagamento é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do Pagamento não é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Pagamento de boleto negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        PagamentoBoleto pagamentoBoleto = TransacaoMapper.toPagamentoBoletoEntity(conta, request);
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
        contaRepository.save(conta);
        transacaoRepository.save(pagamentoBoleto);

        return new BaseResponse(
                "Pagamento de boleto efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarPagamentoBoletoResponse(pagamentoBoleto)
        );
    }

    public BaseResponse criarPagamentoDebito(String idUsuario, String idConta, CriarPagamentoDebitoRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Pagamento no débito negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Pagamento no débito negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Pagamento no débito negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Pagamento no débito negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Pagamento no débito negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do pagamento é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Pagamento no débito negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do Pagamento não é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Pagamento no débito negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        PagamentoDebito pagamentoDebito = TransacaoMapper.toPagamentoDebitoEntity(request, conta);
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
        contaRepository.save(conta);
        transacaoRepository.save(pagamentoDebito);

        return new BaseResponse(
                "Pagamento no débito realizado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarPagamentoDebitoResponse(pagamentoDebito)
        );
    }

    public BaseResponse criarTransferenciaPix(String idUsuario, String idConta, CriarTransferenciaPixRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Transferencia Pix negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Transferencia Pix negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        Optional<Conta> contaEncontradaPelaChavePix = contaRepository.findByChavePix(request.chavePixDestino());
        if (contaEncontradaPelaChavePix.isEmpty()) {
            return new BaseResponse(
                    "Transferencia Pix negada! Conta destino nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }

        Conta contaDestino = contaEncontradaPelaChavePix.get();

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Transferencia Pix negada! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Transferencia Pix negada! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do pagamento é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Transferencia Pix negada! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do Pagamento não é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Transferencia Pix negada! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        Pix pix = TransacaoMapper.toPixEntity(request, conta);
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));
        contaRepository.save(conta);
        transacaoRepository.save(pix);

        return new BaseResponse(
                "Transferência Pix realizada com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarTransferenciaPixResponse(pix, contaDestino)
        );
    }

    public BaseResponse listarTransacoesPorConta(String idUsuario, String idConta) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta mao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Aqui eu busco todas as transacoes pelo idConta
        List<Deposito> depositos = depositoRepository.findByContaId(conta.getId());
        List<Saque> saques = saqueRepository.findByContaId(conta.getId());
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByContaId(conta.getId());
        List<Transferencia> transferenciasEnviadas = transferenciaRepository.findByContaOrigem_Id(conta.getId());
        List<Transferencia> transferenciasRecebidas = transferenciaRepository.findByContaDestino_Id(conta.getId());
        List<CriarCarteira> criarCarteiraList = criarCarteiraRepository.findByContaId(conta.getId());
        List<DeletarCarteira> deletarCarteiraList = deletarCarteiraRepository.findByContaId(conta.getId());
        List<PagamentoBoleto> pagamentoBoletoList = pagamentoBoletoRepository.findByContaId(conta.getId());
        List<PagamentoDebito> pagamentoDebitoList = pagamentoDebitoRepository.findByContaId(conta.getId());
        List<Pix> pixEnviados = pixRepository.findByContaId(conta.getId());
        List<Pix> pixRecebidos = pixRepository.findByChavePixDestino(conta.getChavePix());


        List<TransacaoContaResponse> transacoesResponse = TransacaoMapper.listarTransacoesContaResponse(
                        depositos,
                        saques,
                        contaCarteiraList,
                        transferenciasEnviadas,
                        transferenciasRecebidas,
                        criarCarteiraList,
                        deletarCarteiraList,
                        pagamentoBoletoList,
                        pagamentoDebitoList,
                        pixEnviados,
                        pixRecebidos
                )
                .stream()
                .sorted(Comparator.comparing(TransacaoContaResponse::data).reversed())
                .toList();
        return new BaseResponse("Transacoes", HttpStatus.OK, transacoesResponse);
    }

    public BaseResponse listarTransacoesPorCarteira(String idUsuario, String idConta, String idCarteira) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Busco todas as transacoes da carteira pelo seu ID
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByCarteiraId(carteira.getId());

        //Validação pra ver se a carteira não possui transação
        if (contaCarteiraList.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada na carteira informada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        return new BaseResponse(
                "Transacoes encontradas.",
                HttpStatus.OK,
                TransacaoMapper.listarTransacoesCarteiraResponse(contaCarteiraList)
        );
    }
    public BaseResponse listarTransacoesPorCategoriaPorDia(String idUsuario, String idConta, String categoria, int ano, int mes, int dia){
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<TransacaoPorCategoriaResponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano &&
                                pagamentoBoleto.getData().getMonthValue() == mes &&
                                pagamentoBoleto.getData().getDayOfMonth() == dia && pagamentoBoleto.getCategoria().equalsIgnoreCase(categoria))
                .map(pagamentoBoleto -> new TransacaoPorCategoriaResponse(
                        pagamentoBoleto.getId(),
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor()
                )).toList();

        List<TransacaoPorCategoriaResponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano &&
                                pagamentoDebito.getData().getMonthValue() == mes &&
                                pagamentoDebito.getData().getDayOfMonth() == dia && pagamentoDebito.getCategoria().equalsIgnoreCase(categoria))
                .map(pagamentoDebito -> new TransacaoPorCategoriaResponse(
                        pagamentoDebito.getId(),
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor()
                )).toList();

        List<TransacaoPorCategoriaResponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano &&
                                pix.getData().getMonthValue() == mes &&
                                pix.getData().getDayOfMonth() == dia && pix.getCategoria().equalsIgnoreCase(categoria))
                .map(pix -> new TransacaoPorCategoriaResponse(
                        pix.getId(),
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor()
                ))
                .toList();

        List<TransacaoPorCategoriaResponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano &&
                                transferencia.getData().getMonthValue() == mes &&
                                transferencia.getData().getDayOfMonth() == dia && transferencia.getCategoria().equalsIgnoreCase(categoria))
                .map(transferencia -> new TransacaoPorCategoriaResponse(
                        transferencia.getId(),
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor()
                ))
                .toList();

        List<TransacaoPorCategoriaResponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada na data " + dia + "/" + mes + "/" + ano + " da categoria " + categoria,
                    HttpStatus.OK,
                    null
            );
        }

        return new BaseResponse(
                "Gastos encontrados na data " + dia + "/" + mes + "/" + ano + " da categoria " + categoria,
                HttpStatus.OK,
                todosGastos
        );

    }
    public BaseResponse listarTransacoesPorCategoriaPorMes(String idUsuario, String idConta, String categoria, int ano, int mes){
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<TransacaoPorCategoriaResponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano &&
                                pagamentoBoleto.getData().getMonthValue() == mes
                                && pagamentoBoleto.getCategoria().equalsIgnoreCase(categoria))
                .map(pagamentoBoleto -> new TransacaoPorCategoriaResponse(
                        pagamentoBoleto.getId(),
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor()
                )).toList();

        List<TransacaoPorCategoriaResponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano &&
                                pagamentoDebito.getData().getMonthValue() == mes
                                && pagamentoDebito.getCategoria().equalsIgnoreCase(categoria))
                .map(pagamentoDebito -> new TransacaoPorCategoriaResponse(
                        pagamentoDebito.getId(),
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor()
                )).toList();

        List<TransacaoPorCategoriaResponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano &&
                                pix.getData().getMonthValue() == mes
                                && pix.getCategoria().equalsIgnoreCase(categoria))
                .map(pix -> new TransacaoPorCategoriaResponse(
                        pix.getId(),
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor()
                ))
                .toList();

        List<TransacaoPorCategoriaResponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano &&
                                transferencia.getData().getMonthValue() == mes
                                && transferencia.getCategoria().equalsIgnoreCase(categoria))
                .map(transferencia -> new TransacaoPorCategoriaResponse(
                        transferencia.getId(),
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor()
                ))
                .toList();

        List<TransacaoPorCategoriaResponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada no mes " + mes + "/" + ano + " da categoria " + categoria,
                    HttpStatus.OK,
                    null
            );
        }

        return new BaseResponse(
                "Gastos encontrados no mes " + mes + "/" + ano + " da categoria " + categoria,
                HttpStatus.OK,
                todosGastos
        );
    }
    public BaseResponse categoriasMaisUsadasPorContaMesAno(String idUsuario, String idConta, int ano, int mes) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        // 1. Buscar TODAS as transações de saída com categoria
        List<TransacaoComCategoria> todasAsTransacoesDeSaida = new ArrayList<>();
        todasAsTransacoesDeSaida.addAll(transferenciaRepository.findByContaOrigem_Id(idConta));
        todasAsTransacoesDeSaida.addAll(pagamentoBoletoRepository.findByContaId(idConta));
        todasAsTransacoesDeSaida.addAll(pagamentoDebitoRepository.findByContaId(idConta));
        todasAsTransacoesDeSaida.addAll(pixRepository.findByContaId(idConta));

        if (todasAsTransacoesDeSaida.isEmpty()) {
            return new BaseResponse("Nenhuma transação com categoria encontrada.", HttpStatus.NOT_FOUND, null);
        }

        // O resto da lógica de stream funcionará como esperado
        List<CategoriaResponse> categoriaResponses = todasAsTransacoesDeSaida.stream()
                .filter(t -> t.getData().getYear() == ano && t.getData().getMonthValue() == mes)
                .filter(t -> t.getCategoria() != null && !t.getCategoria().isBlank())
                .collect(Collectors.groupingBy(
                        TransacaoComCategoria::getCategoria,
                        Collectors.mapping(TransacaoComCategoria::getValor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(e -> new CategoriaResponse(e.getKey(), e.getValue()))
                .sorted((c1, c2) -> c2.valor().compareTo(c1.valor()))
                .toList();

        if (categoriaResponses.isEmpty()) {
            return new BaseResponse("Nenhuma categoria de gasto encontrada para o período.", HttpStatus.OK, null);
        }

        return new BaseResponse("Categorias encontradas.", HttpStatus.OK, categoriaResponses);
    }

    public BaseResponse calcularGastoDaContaPorMes(String idUsuario, String idConta, int ano, int mes) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }


        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<GastosReponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano &&
                                pagamentoBoleto.getData().getMonthValue() == mes)
                .map(pagamentoBoleto -> new GastosReponse(
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor(),
                        pagamentoBoleto.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano &&
                                pagamentoDebito.getData().getMonthValue() == mes)
                .map(pagamentoDebito -> new GastosReponse(
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor(),
                        pagamentoDebito.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano &&
                                pix.getData().getMonthValue() == mes)
                .map(pix -> new GastosReponse(
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor(),
                        pix.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano &&
                                transferencia.getData().getMonthValue() == mes)
                .map(transferencia -> new GastosReponse(
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor(),
                        transferencia.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        /*
        JSON todosGastos
        {
            "data": " ",
            "tipo": " ",
            "valor": " ",
            "categoria": " "
         */

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada no mes " + mes + "/" + ano,
                    HttpStatus.OK,
                    null
            );
        }

        //Organização de todos os gastos por ordem da data mais recente para mais antiga
        todosGastos = todosGastos.stream()
                .sorted(Comparator.comparing(GastosReponse::data).reversed())
                .toList();


        // Agrupa todas as transações por dia (sem horário)
        Map<LocalDate, List<GastosReponse>> gastosPorDia = todosGastos.stream()
                .collect(Collectors.groupingBy(g -> g.data().toLocalDate()));
        /*
        JSON gastosPorDia:
        {
            "data": " ",
            "gastosPorDia": {
                                {   "data": " ",
                                    "tipo": " ",
                                    "valor": " ",
                                    "categoria": " "
                                }
                            }
        }
         */

        // Transforma cada grupo (dia) em um objeto com total e lista de transações
        List<DiaDetalhadoResponse> gastosDetalhados = gastosPorDia.entrySet().stream()
                .map(entry -> {
                    LocalDate data = entry.getKey();
                    List<GastosReponse> transacoes = entry.getValue();

                    BigDecimal total = transacoes.stream()
                            .map(GastosReponse::valor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new DiaDetalhadoResponse(data, total, transacoes);
                })
                .sorted(Comparator.comparing(DiaDetalhadoResponse::data).reversed())
                .toList();

        return new BaseResponse(
                "Gastos encontrados.",
                HttpStatus.OK,
                gastosDetalhados
        );

    }

    public BaseResponse calcularGastoDaContaPorMesPorTipoTransacao(String idUsuario, String idConta, int ano, int mes){
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<GastosReponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano &&
                                pagamentoBoleto.getData().getMonthValue() == mes)
                .map(pagamentoBoleto -> new GastosReponse(
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor(),
                        pagamentoBoleto.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano &&
                                pagamentoDebito.getData().getMonthValue() == mes)
                .map(pagamentoDebito -> new GastosReponse(
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor(),
                        pagamentoDebito.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano &&
                                pix.getData().getMonthValue() == mes)
                .map(pix -> new GastosReponse(
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor(),
                        pix.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano &&
                                transferencia.getData().getMonthValue() == mes)
                .map(transferencia -> new GastosReponse(
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor(),
                        transferencia.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada no mes " + mes + "/" + ano,
                    HttpStatus.OK,
                    null
            );
        }



        List<TipoGastoEValorResponse> gastosReponses = todosGastos
                .stream()
                .collect(Collectors.groupingBy(GastosReponse::tipo,
                        Collectors.mapping(GastosReponse::valor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                                )).entrySet().stream().map(gastoResponse ->
                        new TipoGastoEValorResponse(gastoResponse.getKey(), gastoResponse.getValue())).toList();

        return new BaseResponse(
                "Gastos encontrados.",
                HttpStatus.OK,
                gastosReponses
        );


    }

    public BaseResponse calcularGastoDaContaPorAno(String idUsuario, String idConta, int ano) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }


        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<GastosReponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano)
                .map(pagamentoBoleto -> new GastosReponse(
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor(),
                        pagamentoBoleto.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano)
                .map(pagamentoDebito -> new GastosReponse(
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor(),
                        pagamentoDebito.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano)
                .map(pix -> new GastosReponse(
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor(),
                        pix.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano)
                .map(transferencia -> new GastosReponse(
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor(),
                        transferencia.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada no ano " + ano,
                    HttpStatus.OK,
                    null
            );
        }

        List<TipoGastoEValorResponse> gastosReponses = todosGastos
                .stream()
                .collect(Collectors.groupingBy(GastosReponse::tipo,
                        Collectors.mapping(GastosReponse::valor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(gastoResponse ->
                        new TipoGastoEValorResponse(gastoResponse.getKey(), gastoResponse.getValue())).toList();

        return new BaseResponse(
                "Gastos encontrados.",
                HttpStatus.OK,
                todosGastos
        );

    }

    public BaseResponse calcularGastoDaContaPorDia(String idUsuario, String idConta, int ano, int mes, int dia) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }


        List<PagamentoBoleto> gastosBoleto = pagamentoBoletoRepository.findByContaId(idConta);
        List<PagamentoDebito> gastosDebito = pagamentoDebitoRepository.findByContaId(idConta);
        List<Pix> gastosPix = pixRepository.findByContaId(idConta);
        List<Transferencia> gastosTransferencia = transferenciaRepository.findByContaOrigem_Id(idConta);

        List<GastosReponse> todosGastosBoletos = gastosBoleto
                .stream()
                .filter(pagamentoBoleto ->
                        pagamentoBoleto.getData().getYear() == ano &&
                                pagamentoBoleto.getData().getMonthValue() == mes &&
                                pagamentoBoleto.getData().getDayOfMonth() == dia)
                .map(pagamentoBoleto -> new GastosReponse(
                        pagamentoBoleto.getData(),
                        pagamentoBoleto.getTipo(),
                        pagamentoBoleto.getValor(),
                        pagamentoBoleto.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosDebito = gastosDebito
                .stream()
                .filter(pagamentoDebito ->
                        pagamentoDebito.getData().getYear() == ano &&
                                pagamentoDebito.getData().getMonthValue() == mes &&
                                pagamentoDebito.getData().getDayOfMonth() == dia)
                .map(pagamentoDebito -> new GastosReponse(
                        pagamentoDebito.getData(),
                        pagamentoDebito.getTipo(),
                        pagamentoDebito.getValor(),
                        pagamentoDebito.getCategoria()
                )).toList();

        List<GastosReponse> todosGastosPix = gastosPix
                .stream()
                .filter(pix ->
                        pix.getData().getYear() == ano &&
                                pix.getData().getMonthValue() == mes &&
                                pix.getData().getDayOfMonth() == dia)
                .map(pix -> new GastosReponse(
                        pix.getData(),
                        pix.getTipo(),
                        pix.getValor(),
                        pix.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastosTransferencia = gastosTransferencia
                .stream()
                .filter(transferencia ->
                        transferencia.getData().getYear() == ano &&
                                transferencia.getData().getMonthValue() == mes &&
                                transferencia.getData().getDayOfMonth() == dia)
                .map(transferencia -> new GastosReponse(
                        transferencia.getData(),
                        transferencia.getTipo(),
                        transferencia.getValor(),
                        transferencia.getCategoria()
                ))
                .toList();

        List<GastosReponse> todosGastos = new ArrayList<>();
        todosGastos.addAll(todosGastosBoletos);
        todosGastos.addAll(todosGastosDebito);
        todosGastos.addAll(todosGastosPix);
        todosGastos.addAll(todosGastosTransferencia);

        if (todosGastos.isEmpty()) {
            return new BaseResponse(
                    "Nenhum gasto encontrada na data " + dia + "/" + mes + "/" + ano,
                    HttpStatus.OK,
                    null
            );
        }

        List<TipoGastoEValorResponse> gastosReponses = todosGastos
                .stream()
                .collect(Collectors.groupingBy(GastosReponse::tipo,
                        Collectors.mapping(GastosReponse::valor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(gastoResponse ->
                        new TipoGastoEValorResponse(gastoResponse.getKey(), gastoResponse.getValue())).toList();

        return new BaseResponse(
                "Gastos encontrados.",
                HttpStatus.OK,
                gastosReponses
        );

    }

    public BaseResponse buscarTransacao (String idUsuario, String idConta, String idTransacao){
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        Optional<Transacao> transacaoEncontrada = transacaoRepository.findById(idTransacao);
        if (transacaoEncontrada.isEmpty()){
            return new BaseResponse(
                    "Transação nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Transacao transacao = transacaoEncontrada.get();

        if (transacao.getTipo().equalsIgnoreCase("DEPOSITO_CONTA")) {
            Deposito deposito = (Deposito) transacao;

            if (!deposito.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoDepositoContaResponse(
                            deposito.getId(),
                            deposito.getTipo(),
                            deposito.getData(),
                            deposito.getValor(),
                            deposito.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("SAQUE_CONTA")) {
            Saque saque = (Saque) transacao;

            if (!saque.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoSaqueContaResponse(
                            saque.getId(),
                            saque.getTipo(),
                            saque.getData(),
                            saque.getValor(),
                            saque.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("DEPOSITO_CARTEIRA")) {
            Conta_Carteira depositoCarteira = (Conta_Carteira) transacao;

            if (!depositoCarteira.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoDepositoCarteiraResponse(
                            depositoCarteira.getId(),
                            depositoCarteira.getTipo(),
                            depositoCarteira.getData(),
                            depositoCarteira.getValor(),
                            depositoCarteira.getConta().getNumero(),
                            depositoCarteira.getCarteira().getId()));
        }

        if (transacao.getTipo().equalsIgnoreCase("SAQUE_CARTEIRA")) {
            Conta_Carteira saqueCarteira = (Conta_Carteira) transacao;

            if (!saqueCarteira.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoSaqueCarteiraResponse(
                            saqueCarteira.getId(),
                            saqueCarteira.getTipo(),
                            saqueCarteira.getData(),
                            saqueCarteira.getValor(),
                            saqueCarteira.getConta().getNumero(),
                            saqueCarteira.getCarteira().getId()));
        }

        if (transacao.getTipo().equalsIgnoreCase("CRIAR_CARTEIRA")) {
            CriarCarteira criarCarteira = (CriarCarteira) transacao;

            if (!criarCarteira.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoCriarCarteiraResponse(
                            criarCarteira.getId(),
                            criarCarteira.getTipo(),
                            criarCarteira.getData(),
                            criarCarteira.getValor(),
                            criarCarteira.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("DELETAR_CARTEIRA")) {
            DeletarCarteira deletarCarteira = (DeletarCarteira) transacao;

            if (!deletarCarteira.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoDeletarCarteiraResponse(
                            deletarCarteira.getId(),
                            deletarCarteira.getTipo(),
                            deletarCarteira.getData(),
                            deletarCarteira.getValor(),
                            deletarCarteira.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("PAGAMENTO_BOLETO")) {
            PagamentoBoleto pagamentoBoleto = (PagamentoBoleto) transacao;

            if (!pagamentoBoleto.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoPagamentoBoletoResponse(
                            pagamentoBoleto.getId(),
                            pagamentoBoleto.getTipo(),
                            pagamentoBoleto.getData(),
                            pagamentoBoleto.getValor(),
                            pagamentoBoleto.getCodigoBarras(),
                            pagamentoBoleto.getDataVencimento(),
                            pagamentoBoleto.getNomeBeneficiario(),
                            pagamentoBoleto.getInstituicaoFinanceira(),
                            pagamentoBoleto.getCategoria(),
                            pagamentoBoleto.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("PAGAMENTO_DEBITO")) {
            PagamentoDebito pagamentoDebito = (PagamentoDebito) transacao;

            if (!pagamentoDebito.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoPagamentoDebitoResponse(
                            pagamentoDebito.getId(),
                            pagamentoDebito.getTipo(),
                            pagamentoDebito.getData(),
                            pagamentoDebito.getValor(),
                            pagamentoDebito.getNomeEstabelecimento(),
                            pagamentoDebito.getCategoria(),
                            pagamentoDebito.getConta().getNumero()));
        }

        if (transacao.getTipo().equalsIgnoreCase("PIX")) {
            Pix pix = (Pix) transacao;

            if (!pix.getConta().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            Optional<Conta> contaDestino = contaRepository.findByChavePix(pix.getChavePixDestino());

            if(contaDestino.isPresent()){

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoPixResponse(
                            pix.getId(),
                            pix.getTipo(),
                            pix.getData(),
                            pix.getValor(),
                            pix.getChavePixDestino(),
                            contaDestino.get().getUsuario().getNome(),
                            pix.getCategoria(),
                            pix.getConta().getNumero()));
            }
        }

        if (transacao.getTipo().equalsIgnoreCase("PAGAMENTO")) {
            Transferencia transferencia = (Transferencia) transacao;

            if (!transferencia.getContaOrigem().getId().equals(conta.getId())) {
                return new BaseResponse(
                        "Transação não pertence à conta informada.",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            return new BaseResponse("Transação encontrada!",
                    HttpStatus.OK,
                    new TransacaoTEDResponse(
                            transferencia.getId(),
                            transferencia.getTipo(),
                            transferencia.getData(),
                            transferencia.getValor(),
                            transferencia.getContaOrigem().getNumero(),
                            transferencia.getNumeroContaDestino(),
                            transferencia.getCategoria()));
        }

        else {
            return new BaseResponse(
                    "Tipo de transação inválida.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }



    }




}
