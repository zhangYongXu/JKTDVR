package com.geeksworld.jktdvr.views.wheel.widget.adapters;

import android.content.Context;



import java.util.List;

/**
 * Created by xhs on 2018/5/14.
 */

public class ListWheelAdapter extends AbstractWheelTextAdapter {
    // items
    //private List<QuestionProblemLevelOptionModel> list;


//    public ListWheelAdapter(Context context,List<QuestionProblemLevelOptionModel> inList) {
//        super(context);
//        this.list = inList;
//    }

    public ListWheelAdapter(Context context) {
        super(context);
    }

 //   @Override
//    public CharSequence getItemText(int index) {
//        if (index >= 0 && index < list.size()) {
//            QuestionProblemLevelOptionModel questionProblemLevelOptionModel = list.get(index);
//            String title = questionProblemLevelOptionModel.getQues_module_problem_level_optionName();
//            return title;
//        }
//        return null;
//    }

    @Override
    protected CharSequence getItemText(int index) {
        return null;
    }

    @Override
    public int getItemsCount() {
        return 0;
    }



}
