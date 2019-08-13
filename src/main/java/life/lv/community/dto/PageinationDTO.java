package life.lv.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PageinationDTO<T> {
    private List<T> data;
    private boolean showFirstPage;
    private boolean showPrePage;
    private boolean showEndPage;
    private boolean showNextPage;
    private Integer page;
    private List<Integer> pages= new ArrayList<>();
    private Integer totalPage;
    public void setPageination(Integer page,Integer totalPage) {
        this.page=page;
        this.totalPage=totalPage;
        //展示的页数
        /**
         *比如
         * totalPage=20页
         * totalCount=98条
         *
         *
         * 1    1 2 3 4 f
         * 2    1 2 3 4 5
         * 3    1 2 3 4 5 6
         * 4    1 2 3 4 5 6 7
         * 5    2 3 4 5 6 7 8
         * 6    3 4 5 6 7 8 9
         * 7    4 5 6 7 8 9 10
         * 8    5 6 7 8 9 10 11
         *  ...
         * 15       12 13 14 15 16 17 18
         * 16       13 14 15 16 17 18 19
         * 17       14 15 16 17 18 19 20
         * 18          15 16 17 18 19 20
         * 19             16 17 18 19 20
         * 20                17 18 19 20
         */
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
        //是否展示上一页
        if(page==1){
            showPrePage=false;
        }else{
            showPrePage=true;
        }
        //是否展示下一页
        if(page==totalPage){
            showNextPage=false;
        }else{
            showNextPage=true;
        }

        //是否展示第一页
        if(pages.contains(1)){
            showFirstPage=false;
        }else{
            showFirstPage=true;
        }
        //是否展示最后一页
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }




    }
}
