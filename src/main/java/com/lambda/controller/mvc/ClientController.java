package com.lambda.controller.mvc;

import com.lambda.constant.GrantType;
import com.lambda.model.dto.ClientDTO;
import com.lambda.model.dto.ViewMessage;
import com.lambda.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/client")
@SuppressWarnings("deprecation")
@SessionAttributes({"newClient", "existedClient", "grantTypes"})
public class ClientController {

    private final ClientService clientService;

    private final ServletContext servletContext;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientController(ClientService clientService, ServletContext servletContext,
                            PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.servletContext = servletContext;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("newClient")
    public ClientDTO clientDetail(@CookieValue(value = "newClient", required = false) String cookie) {
        BaseClientDetails prototype = new BaseClientDetails
                ("", null, null, null, null, null);
        ClientDTO clientDTO = new ClientDTO(prototype);
        this.clientService.patchCookieToForm(cookie, clientDTO);
        return clientDTO;
    }

    @ModelAttribute("grantTypes")
    public GrantType[] grantTypes() {
        return GrantType.values();
    }

    @GetMapping("/list")
    public ModelAndView getClientList() {
        List<ClientDTO> clientList = this.clientService.findAll();
        return new ModelAndView("client/client-list", "clientList", clientList);
    }

    @GetMapping("/create")
    public String createClient(@ModelAttribute("newClient") ClientDTO clientDTO,
                               @ModelAttribute("grantTypes") GrantType[] grantTypes) {
        return "client/client-create";
    }

    @PostMapping("/create")
    public RedirectView createClient(@ModelAttribute("newClient") ClientDTO clientDTO,
                                     RedirectAttributes redirectAttributes, SessionStatus sessionStatus,
                                     HttpServletResponse response) {
        String redirectUrl;
        try {
            Cookie cookie = this.clientService.createCookie(clientDTO);
            response.addCookie(cookie);
            clientDTO.setClientSecret(this.passwordEncoder.encode(clientDTO.getClientSecret()));
            this.clientService.create(clientDTO);
            redirectUrl = this.servletContext.getContextPath() + "/client/list";
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Client created successfully!", true));
            return new RedirectView(redirectUrl);
        } catch (Exception ex) {
            redirectUrl = this.servletContext.getContextPath() + "/client/create";
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage(String.format("Failed to create client: %s", ex.getMessage()), false));
            return new RedirectView(redirectUrl);
        }
    }

    @GetMapping("/update/{id}")
    public String updateClient(@PathVariable("id") String id, @ModelAttribute("grantTypes") GrantType[] grantTypes,
                               ModelMap modelMap, RedirectAttributes redirectAttributes) {
        try {
            ClientDTO clientDTO = this.clientService.findById(id);
            modelMap.addAttribute("existedClient", clientDTO);
            return "client/client-update";
        } catch (Exception ex) {
            String redirectUrl = "/client/list";
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage(String.format("Failed to delete client secret: %s", ex.getMessage()), false));
            return "redirect:" + redirectUrl;
        }
    }

    @PostMapping("/update")
    public RedirectView updateClient(@ModelAttribute("existedClient") ClientDTO clientDTO,
                                     RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        String redirectUrl;
        try {
            this.clientService.update(clientDTO);
            redirectUrl = this.servletContext.getContextPath() + "/client/list";
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Client updated successfully!", true));
            return new RedirectView(redirectUrl);
        } catch (Exception ex) {
            redirectUrl = this.servletContext.getContextPath() + "/client/update/" + clientDTO.getClientId();
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage(String.format("Failed to update client: %s", ex.getMessage()), false));
            return new RedirectView(redirectUrl);
        }
    }

    @PostMapping("/update-secret/{id}")
    public RedirectView updateClientSecret(@PathVariable("id") String id,
                                           @RequestParam("newSecret") String secret,
                                           RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        String redirectUrl = this.servletContext.getContextPath() + "/client/list";
        try {
            String encoded = this.passwordEncoder.encode(secret);
            this.clientService.updateSecret(id, encoded);
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Client secret updated successfully!", true));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage(String.format("Failed to update client secret: %s", ex.getMessage()), false));
        }
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/delete")
    public RedirectView deleteClient(@RequestParam("id") String id, RedirectAttributes redirectAttributes,
                                     SessionStatus sessionStatus) {
        String redirectUrl = this.servletContext.getContextPath() + "/client/list";
        try {
            this.clientService.deleteById(id);
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Client deleted successfully!", true));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage(String.format("Failed to delete client secret: %s", ex.getMessage()), false));
        }
        return new RedirectView(redirectUrl);
    }
}
