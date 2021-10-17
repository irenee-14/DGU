package org.techtown.dgu.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.dgu.DGUDB;
import org.techtown.dgu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubTestAdapter extends RecyclerView.Adapter<SubTestAdapter.subtestViewHolder>{

    private ArrayList<SubTestItem> subTestList;
    private Context mContext;
    private DGUDB db;

    public SubTestAdapter(Context context, ArrayList<SubTestItem> st) {
        this.mContext = context;
        this.subTestList = st;
        this.db = new DGUDB(mContext);
    }

    public class subtestViewHolder extends RecyclerView.ViewHolder{
        protected TextView subtestname;
        protected TextView subtestdday;

        public subtestViewHolder(View view){
            super(view);
            subtestname = view.findViewById(R.id.subtestname);
            subtestdday = view.findViewById(R.id.subtestdday);
            ///
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //현재 리스트 클릭한 아이템 위치
                    SubTestItem subtest=subTestList.get(curPos);

                    String[] strChoiceItems={"시험 수정하기","시험 삭제하기"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택 해주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                //수정하기
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.test_input);

                                EditText subtestNameInput=dialog.findViewById(R.id.subtestNameInput);
                                EditText subtestDdayInput=dialog.findViewById(R.id.subtestDdayInput);

                                subtestNameInput.setText(subtest.getSubtestname());
                                subtestDdayInput.setText(subtest.getTestDday());

                                Button subtestbtn_ok=dialog.findViewById(R.id.subtestInputButton);
                                subtestbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String subtestname=subtestNameInput.getText().toString();
                                        String subtestdday=subtestDdayInput.getText().toString();

                                        String beforesubtestname=subtest.getSubtestname();

                                        String currentTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                        String id = subtest.getId();

                                        //update UI
                                        subtest.setSubtestname(subtestname);
                                        subtest.setTestDday(subtestdday);

                                        notifyItemChanged(curPos,subtest);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"시험 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();
                            }
                            else if(position==1){
                                //delete table
                                String id = subtest.getId();

                                //delete UI
                                subTestList.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"시험 삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();

                }
            });

            ///
        }
    }

    @NonNull
    @Override
    public subtestViewHolder onCreateViewHolder(ViewGroup viewGroup,int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_item,viewGroup, false);

        return new SubTestAdapter.subtestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(subtestViewHolder SubtestviewHolder,int position)
    {
        SubtestviewHolder.subtestname.setText(subTestList.get(position).getSubtestname());
        SubtestviewHolder.subtestdday.setText(subTestList.get(position).getTestDday());
    }

    @Override
    public int getItemCount() {
        if(subTestList!=null)
        return subTestList.size();
        else return 0;
    }

    // 현재 어댑터에 새로운 아이템을 전달받아 추가하는 목적
    public void addtestItem(String subid,SubTestItem _item){
        int addpos = subTestList.size();
        _item.setId(db.insertTest(subid,_item.getSubtestname(),_item.getTestDday()));
        subTestList.add(_item);
        notifyItemInserted(addpos);
    }
}
