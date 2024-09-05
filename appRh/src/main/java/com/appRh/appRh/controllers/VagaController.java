package com.appRh.appRh.controllers;

import com.appRh.appRh.model.Candidato;
import com.appRh.appRh.model.Vaga;
import com.appRh.appRh.repository.CandidatoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.appRh.appRh.repository.VagaRepository;
import org.springframework.stereotype.Controller;

@Controller
public class VagaController {

    private VagaRepository vr;
    private CandidatoRepository cr;

    //Serve para cadastrar vagas
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
    public String form() {

        return "vaga/formVaga";
    }

    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Campos em falta...");

            return "redirect:/cadastrarVaga";
        }

        vr.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
        return "redirect:/cadastrarVaga";

    }
    //Listagem de vagas

    @RequestMapping("/vagas")
    public ModelAndView listaVagas() {
        ModelAndView modelAndView = new ModelAndView("vaga/listaVaga");
        Iterable<Vaga> vagas = vr.findAll();
        modelAndView.addObject("vagas", vagas);
        return modelAndView;

    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesVaga(@PathVariable long codigo){
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView modelAndView = new ModelAndView("vaga/detalhesVaga");
        modelAndView.addObject("vaga", vaga);

        Iterable<Candidato> candidatos = cr.findByVaga(vaga);
        modelAndView.addObject("candidatos", candidatos);
        return modelAndView;
    }

    //Deleta Vagas

    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        vr.delete(vaga);
        return "redirect:/vagas";
    }

    public String detalhesVagaPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/{codigo}";

        }

        //Quando o rg estiver duplicado
        if (cr.findByRg(candidato.getRg()) != null) {
            attributes.addFlashAttribute("mensagem erro", "RG duplicado");
            return "redirect:/{codigo}";
        }

        Vaga vaga = vr.findByCodigo(codigo);
        candidato.setVaga(vaga);
        cr.save(candidato);
        attributes.addFlashAttribute("mensagem", "Candidato adcionado com sucesso!");
        return "redirect:/{codigo}";
    }

    //Deleta candidato pelo RG

    @RequestMapping("/deletarCandidato")
    public String deletarCandidado(String rg) {
        Candidato candidato = cr.findByRg(rg);
        Vaga vaga = candidato.getVaga();
        String codigo = "" + vaga.getCodigo();

        cr.delete(candidato);
        return "redirect:/" + codigo;

    }

    //Metodos que atualizam vagas


    //formulario de adição de vaga
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)
    public ModelAndView editarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        ModelAndView modelAndView = new ModelAndView("vaga/update-vaga");
        modelAndView.addObject("vaga", vaga);
        return modelAndView;
    }

    //Update Vaga
    @RequestMapping(value = "/editar-vaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes){
        vr.save(vaga);
        attributes.addFlashAttribute("sucess", "Vaga alterada com sucesso!");

        long codigoLong = vaga.getCodigo();
        String codigo = "" +codigoLong;
        return "redirect:/" +codigo;
    }

}
