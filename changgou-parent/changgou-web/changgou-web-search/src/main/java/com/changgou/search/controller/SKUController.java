package com.changgou.search.controller;

import com.changgou.search.feign.SKUFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
@CrossOrigin
public class SKUController {
    @Autowired
    private SKUFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String, String> searchmap, Model model) {
        Map resultmap = skuFeign.search(searchmap);
        model.addAttribute("resultMap", resultmap);
        model.addAttribute("searchMap", searchmap);

        String[] urls = urls(searchmap);
        model.addAttribute("url", urls[0]);
        model.addAttribute("sorturl", urls[1]);

        Page<SkuInfo> skuInfoPage = new Page<SkuInfo>(Long.parseLong(resultmap.get("totalPages").toString()), Integer.parseInt(resultmap.get("pagenum").toString()), Integer.parseInt(resultmap.get("pagesize").toString()));
//        skuInfoPage.setTotal(Long.parseLong(resultmap.get("totalPages").toString()));
////        skuInfoPage.setCurrentpage(Integer.parseInt(resultmap.get("pagenum").toString()));
////        skuInfoPage.setSize(Integer.parseInt(resultmap.get("pagesize").toString()));
        model.addAttribute("page", skuInfoPage);
        return "search";

    }

    public String[] urls(Map<String, String> searchmap) {
        String url = "/search/list";
        String sorturl = "/search/list";

        if (searchmap != null && searchmap.size() > 0) {
            url += "?";
            sorturl += "?";
            for (Map.Entry<String, String> entry : searchmap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase("pageNum")) {
                    continue;
                }
                url += key + "=" + value + "&";
                if (key.equalsIgnoreCase("sortfiled") || key.equalsIgnoreCase("sorttype") || key.equalsIgnoreCase("pageNum")) {
                    continue;
                }

                sorturl += key + "=" + value + "&";
            }
            url = url.substring(0, url.length() - 1);
            sorturl = sorturl.substring(0, sorturl.length() - 1);
        }
        return new String[]{url, sorturl};
    }
}
