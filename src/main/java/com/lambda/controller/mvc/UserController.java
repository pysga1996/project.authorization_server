package com.lambda.controller.mvc;

import com.lambda.error.BusinessException;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import com.lambda.model.dto.ViewMessage;
import com.lambda.service.GroupService;
import com.lambda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final GroupService groupService;

    private final MessageSource messageSource;


    @Autowired
    public UserController(UserService userService, GroupService groupService,
                          MessageSource messageSource) {
        this.userService = userService;
        this.groupService = groupService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public ModelAndView register(ModelMap modelMap) {
        UserDTO user = new UserDTO();
        UserProfileDTO userProfile = new UserProfileDTO();
        modelMap.addAttribute("newUser", user);
        modelMap.addAttribute("newUserProfile", userProfile);
        return new ModelAndView("user/register", modelMap);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("newUser") UserDTO user,
                                 BindingResult userBindingResult,
                                 @Valid @ModelAttribute("newUserProfile") UserProfileDTO userProfile,
                                 BindingResult userProfileBindingResult, ModelMap modelMap,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            if (userBindingResult.hasErrors() || userProfileBindingResult.hasErrors()) {
                return new ModelAndView("/user/register", modelMap);
            }
            user.setUserProfile(userProfile);
            this.userService.register(user);
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Register successfully!", true));
            return new ModelAndView("redirect:" + "/homepage.html");
        } catch (BusinessException ex) {
            String msg = this.messageSource.getMessage(ex.getMessage(), null, request.getLocale());
            modelMap.addAttribute("viewMessage", new ViewMessage(msg, false));
            return new ModelAndView("/user/register", modelMap);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unregister")
    public ModelAndView unregister(ModelMap modelMap, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.userService.unregister(username);
            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken("anonymous", "anonymousUser", null));
            redirectAttributes.addFlashAttribute("viewMessage",
                    new ViewMessage("Unregister successfully!", true));
            return new ModelAndView("redirect:" + "/homepage.html");
        } catch (BusinessException ex) {
            String msg = this.messageSource.getMessage(ex.getMessage(), null, request.getLocale());
            modelMap.addAttribute("viewMessage", new ViewMessage(msg, false));
            return new ModelAndView("/user/register", modelMap);
        }
    }

    @PreAuthorize("hasAuthority('VIEW_USER_LIST')")
    @GetMapping("/list")
    public String userList(ModelMap modelMap, @PageableDefault Pageable pageable) {
        modelMap.addAttribute("groupList", this.groupService.groupList());
        modelMap.addAttribute("userList", this.userService.getUserList(pageable));
        return "/user/user-list";
    }
}
