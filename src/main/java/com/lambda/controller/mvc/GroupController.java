package com.lambda.controller.mvc;

import com.lambda.error.BusinessException;
import com.lambda.model.domain.Group;
import com.lambda.model.dto.ViewMessage;
import com.lambda.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Log4j2
@Controller
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PreAuthorize("hasAuthority('VIEW_GROUP_LIST')")
    @GetMapping("/list")
    public String groupList(ModelMap modelMap) {
        modelMap.addAttribute("groupList", this.groupService.groupListWithInfo());
        return "user/group-list";
    }

    @PreAuthorize("hasAuthority('CREATE_GROUP')")
    @GetMapping("/create")
    public String showGroupCreateForm(ModelMap modelMap) {
        modelMap.addAttribute("newGroup", new Group());
        return "user/group-create";
    }

    @PreAuthorize("hasAuthority('CREATE_GROUP')")
    @PostMapping("/create")
    public ModelAndView createGroup(@ModelAttribute("newGroup") Group group,
        RedirectAttributes redirectAttributes) {
        try {
            this.groupService.createGroup(group);
            redirectAttributes.addFlashAttribute("viewMessage",
                new ViewMessage("Create group successfully!", true));
            return new ModelAndView("redirect:/group/list");
        } catch (BusinessException ex) {
            log.error("Error create group!", ex);
            return new ModelAndView("user/group-create", "viewMessage",
                new ViewMessage(ex.getMessage(), false));
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_GROUP')")
    @GetMapping("/update/{id}")
    public String showGroupUpdateForm(@PathVariable("id") Long id, ModelMap modelMap,
        @RequestParam(value = "authority-page-number", defaultValue = "1") int authorityPageNumber,
        @RequestParam(value = "authority-page-size", defaultValue = "5") int authorityPageSize,
        @RequestParam(value = "user-page-number", defaultValue = "1") int userPageNumber,
        @RequestParam(value = "user-page-size", defaultValue = "5") int userPageSize) {
        Group group = this.groupService.findGroupById(id, authorityPageNumber, authorityPageSize,
            userPageNumber, userPageSize);
        modelMap.addAttribute("existedGroup", group);
        return "user/group-update";
    }

    @PreAuthorize("hasAuthority('UPDATE_GROUP')")
    @PostMapping("/rename")
    public RedirectView renameGroup(@ModelAttribute("existedGroup") Group group,
        RedirectAttributes redirectAttributes) {
        this.groupService.renameGroup(group);
        redirectAttributes.addFlashAttribute("viewMessage",
            new ViewMessage("Rename group successfully!", true));
        return new RedirectView("/group/update/" + group.getId(), true);
    }

    @PreAuthorize("hasAuthority('UPDATE_GROUP')")
    @PostMapping(value = "/add-authority", params = {"authority", "addAuthority"})
    public RedirectView addAuthorityToGroup(@ModelAttribute("existedGroup") Group group,
        @RequestParam("authority") String authority,
        RedirectAttributes redirectAttributes) {
        try {
            this.groupService.addAuthorityToGroup(group.getName(), authority);
            redirectAttributes.addFlashAttribute("viewMessage",
                new ViewMessage("Add authority to group successfully!", true));
            return new RedirectView("/group/update/" + group.getId(), true);
        } catch (BusinessException ex) {
            log.error("Error while adding authority to group!", ex);
            redirectAttributes.addFlashAttribute("viewMessage",
                new ViewMessage(ex.getMessage(), false));
            return new RedirectView("/group/update/" + group.getId(), true);
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_GROUP')")
    @PostMapping(value = "/remove-authority")
    public RedirectView removeAuthorityFromGroup(@ModelAttribute("existedGroup") Group group,
        @RequestParam("authority") String authority,
        RedirectAttributes redirectAttributes) {
        try {
            this.groupService.removeAuthority(group.getName(), authority);
            redirectAttributes.addFlashAttribute("viewMessage",
                new ViewMessage("Remove authority from group successfully!", true));
            return new RedirectView("/group/update/" + group.getId(), true);
        } catch (BusinessException ex) {
            log.error("Error while removing authority from group!", ex);
            redirectAttributes.addFlashAttribute("viewMessage",
                new ViewMessage(ex.getMessage(), false));
            return new RedirectView("/group/update/" + group.getId(), true);
        }
    }

    @PreAuthorize("hasAuthority('DELETE_GROUP')")
    @PostMapping("/remove")
    public RedirectView deleteGroup(@RequestParam("group-name") String groupName,
        RedirectAttributes redirectAttributes) {
        this.groupService.removeGroup(groupName);
        redirectAttributes.addFlashAttribute("viewMessage",
            new ViewMessage("Remove group successfully!", true));
        return new RedirectView("/group/list", true);
    }
}
