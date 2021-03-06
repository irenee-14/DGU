package org.techtown.dgu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class studysubAdapter extends RecyclerView.Adapter<studysubAdapter.studysubViewHolder>{

    private ArrayList<studysub> subList;
    private Context mContext;

    private Subject_DB mSubject_DB;

    protected RecyclerView hwrecycler;
    private homework_DB mHomework_DB;
    ArrayList<homework> homeworkList;
    private homeworkAdapter mHomeworkAdapter;


    protected RecyclerView testrecycler;
    private subtest_DB mSubTest_DB;
    ArrayList<subtest> subTestList;
    private subtestAdapter msubtestAdapter;


    
    public studysubAdapter(Context context, ArrayList<studysub> subList){
        this.subList = subList;
        this.mContext = context;
        mSubject_DB=new Subject_DB(context);
    }

    @Override
    public studysubViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_item,viewGroup,false);
        studysubViewHolder sh = new studysubViewHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(studysubViewHolder StudysubviewHolder, int i){
        final int id=subList.get(i).getId();
        final String subname = subList.get(i).getSubname();
        final String subtime = subList.get(i).getSubtime();

        ArrayList homeworkList = subList.get(i).getHwList();
        ArrayList subTestList = subList.get(i).getTestList();
        StudysubviewHolder.subjectname.setText(subname);
        StudysubviewHolder.subjecttime.setText(subtime);

       // homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, homeworkList);
      //  subtestAdapter subjecttestListDataAdapter = new subtestAdapter(mContext,subTestList);

        StudysubviewHolder.hwrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.hwrecycler.setAdapter(mHomeworkAdapter);
        StudysubviewHolder.testrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        StudysubviewHolder.testrecycler.setAdapter(msubtestAdapter);

    }

    @Override
    public int getItemCount(){
        return subList.size();
    }

    // ?????? ???????????? ????????? ???????????? ???????????? ???????????? ??????
    public void addSubItem(studysub _item) {
            subList.add(0,_item);
            notifyItemInserted(0);
        }



    public class studysubViewHolder extends RecyclerView.ViewHolder{
        protected ImageButton image;
        protected TextView subjectname;
        protected TextView subjecttime;
        protected TextView addhw;
        protected RecyclerView hwrecycler;
        protected TextView addtest;
        protected RecyclerView testrecycler;

        public studysubViewHolder(View view){
            super(view);
            this.image = (ImageButton)view.findViewById(R.id.imageButton);
            this.subjectname = (TextView)view.findViewById(R.id.subjectname);
            this.subjecttime = (TextView)view.findViewById(R.id.subjecttime);

            this.addhw = (TextView)view.findViewById(R.id.addhw);
            this.hwrecycler = (RecyclerView)view.findViewById(R.id.hwrecycler);
            this.addtest = (TextView)view.findViewById(R.id.addtest);
            this.testrecycler = (RecyclerView)view.findViewById(R.id.testrecycler);


            mHomework_DB=new homework_DB((this.hwrecycler.getContext()));
            homeworkAdapter homeworkListDataAdapter = new homeworkAdapter(mContext, homeworkList);
            hwrecycler.setLayoutManager(new LinearLayoutManager(this.hwrecycler.getContext(),LinearLayoutManager.VERTICAL,false));
            hwrecycler.setAdapter(homeworkListDataAdapter);
            homeworkList=new ArrayList<>();


            mSubTest_DB=new subtest_DB((this.testrecycler.getContext()));
            subtestAdapter subjecttestListDataAdapter = new subtestAdapter(mContext,subTestList);
            testrecycler.setLayoutManager(new LinearLayoutManager(this.testrecycler.getContext(),LinearLayoutManager.VERTICAL,false));
            testrecycler.setAdapter(subjecttestListDataAdapter);
            subTestList=new ArrayList<>();



            //load recent DB
            loadhwRecentDB();
            loadtestRecentDB();


////???????????? ???????????????
            addhw.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v){
                        //????????? ?????????
                        Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                        dialog.setContentView(R.layout.homework_input);

                        EditText homeworkNameInput=dialog.findViewById(R.id.homeworkNameInput);
                        EditText homeworkDdayInput=dialog.findViewById(R.id.homeworkDdayInput);
                       

                        Button homeworkInputBtn_ok=dialog.findViewById(R.id.homeworkInputButton);
                        homeworkInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){

                                //Insert Database
                                String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());


                                mHomework_DB.InsetHomework(homeworkNameInput.getText().toString(),homeworkDdayInput.getText().toString());
                                
                                //Insert UI
                                homework hwitem=new homework();
                                hwitem.setHwname(homeworkNameInput.getText().toString());
                                hwitem.setHwDday(homeworkDdayInput.getText().toString());

                                mHomeworkAdapter.addhwItem(hwitem);
                                hwrecycler.setAdapter(mHomeworkAdapter);
                                hwrecycler.smoothScrollToPosition(0);


                                dialog.dismiss();
                                Toast.makeText(mContext,"????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show();


                            }
                        });

                        dialog.show();
                    
                    
                }
            });

////???????????? ???????????????
            ///
            addtest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    //????????? ?????????
                    Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                    dialog.setContentView(R.layout.test_input);

                    EditText subtestNameInput=dialog.findViewById(R.id.subtestNameInput);
                    EditText subtestDdayInput=dialog.findViewById(R.id.subtestDdayInput);


                    Button testInputBtn_ok=dialog.findViewById(R.id.subtestInputButton);
                    testInputBtn_ok.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){

                            //Insert Database
                            String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());


                            mSubTest_DB.InsetSubtest(subtestNameInput.getText().toString(),subtestDdayInput.getText().toString());

                            //Insert UI
                            subtest testitem=new subtest();
                            testitem.setSubtestname(subtestNameInput.getText().toString());
                            testitem.setTestDday(subtestDdayInput.getText().toString());

                            msubtestAdapter.addtestItem(testitem);
                            testrecycler.setAdapter(msubtestAdapter);
                            testrecycler.smoothScrollToPosition(0);

                            dialog.dismiss();
                            Toast.makeText(mContext,"????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show();


                        }
                    });

                    dialog.show();


                }
            });
            ///

////?????? ?????????????????? ????????? ??????
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int curPos=getAdapterPosition(); //?????? ????????? ????????? ????????? ??????
                    studysub studysub=subList.get(curPos);


                    String[] strChoiceItems={"????????????","?????? ????????????","?????? ????????????"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("????????? ????????? ?????? ????????????");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position==0){
                                AttendanceCheck attendanceCheck = new AttendanceCheck();

                                String name = studysub.getSubname();
                                attendanceCheck.setSubName(name);
                                ((MainActivity)view.getContext()).replaceFragment(attendanceCheck);    // ?????? ????????? Fragment??? Instance??? Main?????? ??????

                            }
                            else if(position==1){
                                //????????????
                                Dialog dialog=new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.activity_subject_input);

                                EditText subjectNameInput=dialog.findViewById(R.id.subjectNameInput);
                                EditText weekInput=dialog.findViewById(R.id.weekInput);
                                EditText weekFrequencyInput=dialog.findViewById(R.id.weekFrequencyInput);

                                Button subjectbtn_ok=dialog.findViewById(R.id.subjectInputButton);
                                subjectbtn_ok.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        //update table
                                        String subname=subjectNameInput.getText().toString();
                                        Integer week=parseInt(weekInput.getText().toString());
                                        Integer weekFre=parseInt(weekFrequencyInput.getText().toString());


                                        String currentTime=new SimpleDateFormat("yyyy-MM-dd:mm:ss").format(new Date());
                                        int id = studysub.getId();
                                        mSubject_DB.UpdateTodo(id,subname,week,weekFre);

                                        //update UI
                                        studysub.setSubname(subname);
                                        studysub.setWeek(week);
                                        studysub.setWeekFre(weekFre);
                                        notifyItemChanged(curPos,studysub);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"?????? ????????? ?????????????????????.",Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.show();
                            }
                            else if(position==2){
                                //delete table
                                String subname=studysub.getSubname();
                                int id = studysub.getId();
                                mSubject_DB.DeleteTodo(id);

                                //delete UI
                                subList.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext,"?????? ????????? ?????????????????????.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();

                }
            });


        }

        private void loadhwRecentDB() {
            //???????????? ?????? DB??? ????????????
            homeworkList=mHomework_DB.getHomeworkList();
            if(mHomeworkAdapter==null){
                mHomeworkAdapter=new homeworkAdapter(mContext,homeworkList);
                hwrecycler.setHasFixedSize(true);
                hwrecycler.setAdapter(mHomeworkAdapter);
            }
        }

        private void loadtestRecentDB() {
            //???????????? ?????? DB??? ????????????
            subTestList=mSubTest_DB.getSubTestList();
            if(msubtestAdapter==null){
                msubtestAdapter=new subtestAdapter(mContext,subTestList);
                testrecycler.setHasFixedSize(true);
                testrecycler.setAdapter(msubtestAdapter);
            }
        }

    }
}
