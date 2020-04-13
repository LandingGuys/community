package life.lv.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PageinationDTO<T> {
    private List<T> data;
    private Integer page;
    private List<Integer> pages= new ArrayList<>();
    private Integer totalPage;
    private Integer totalCount;
    public void setPageination(Integer page,Integer totalPage) {
        this.page=page;
        this.totalPage=totalPage;
        pages.add(page);
        for(int i=1;i<=3;i++) {
            if(page-i>0) {
                pages.add(page-i);
            }
            if(page+i<=totalPage) {
                pages.add(page+i);
            }
        }
        Collections.sort(pages);
    }
}
