package com.hetero.heteroiconnect.existingduelist;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sf.json.JSONArray;

@RestController
@RequestMapping("/duelist")
public class IncrementDueListController {

    @Autowired
    private IncrementRepository incrementRepository;

    // ✅ API endpoint: /duelist/fecthincrement
    @PostMapping("fecthincrement")
    public LinkedHashMap<String, Object> fecthincrement() throws Exception {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        JSONArray data = incrementRepository.fecthincrement();  // No parameters now

        response.put("fecthincrement", data);
        return response;
    }
}
