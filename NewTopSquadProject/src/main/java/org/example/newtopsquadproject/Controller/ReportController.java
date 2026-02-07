package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.UserReportDTO;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/message/{id}")
    public ResponseEntity<UserReportDTO> reportMessage(@PathVariable("id") int messageId,
                                                       @RequestBody Map<String, String> description){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserReportDTO userReportDTO = reportService.createMessageReport(messageId, principal.getUserId(), description.get("description"));
        return new ResponseEntity<>(userReportDTO, HttpStatus.CREATED);
    }
}
