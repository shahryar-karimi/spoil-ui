package ir.shahryar.spoilui;

import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class GateWay {
    private static final String dataAnalyzerURL = "http://localhost:7070/analyze";

    @PostMapping("analyze")
    public String analyze(@ModelAttribute SearchBar searchBar, Model model) throws IOException {
        Request request = new Request.Builder()
                .url(dataAnalyzerURL + "?coinName=" + searchBar.getValue())
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        String jsonObject;
        if (responseBody != null) {
            jsonObject = responseBody.string();
        } else {
            throw new IllegalArgumentException();
        }
        DTO dto = new GsonBuilder().create().fromJson(jsonObject, DTO.class);
        model.addAttribute("analysisPrice", dto.message);
        return "show";
    }

    @GetMapping("analyze")
    public String analyze(Model model) {
        SearchBar searchBar = new SearchBar();
        searchBar.setValue("");
        model.addAttribute("searchBar", searchBar);
        return "search";
    }

    @GetMapping("")
    public String home() {
        return "index";
    }
}
