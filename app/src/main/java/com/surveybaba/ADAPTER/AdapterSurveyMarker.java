package com.surveybaba.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormDetailsActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.FileUploadViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

public class AdapterSurveyMarker extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // List
    private List<FormDetailData> viewTypes;
    // Activity
    private Context activity;
    // View Type
    private final int VIEW_TYPE_RADIO_GROUP = 1;
    private final int VIEW_TYPE_SELECT = 2;
    private final int VIEW_TYPE_TEXT = 3;
    private final int VIEW_TYPE_DATE = 4;
    private final int VIEW_TYPE_RATINGS = 5;
    private final int VIEW_TYPE_SCORE = 6;
    private final int VIEW_TYPE_CAMERA = 7;
    private final int VIEW_TYPE_VIDEO = 8;
    private final int VIEW_TYPE_TEXT_AREA = 9;
    private final int VIEW_TYPE_CHECKBOX_GROUP = 10;
    private final int VIEW_TYPE_HEADER = 11;
    private final int VIEW_TYPE_NUMBER = 12;
    private final int VIEW_TYPE_FILE = 13;
    private final int VIEW_TYPE_AUDIO = 14;
    private final int VIEW_TYPE_PARAGRAPH = 15;
    private final int VIEW_TYPE_THUMSUP_UPLOADER = 16;
    private final int VIEW_TYPE_SMILEY_UPLOADER = 17;
    private final int VIEW_TYPE_BAR_CODE         = 18;
    //private final int VIEW_TYPE_FORM_BG_COLOR    = 18;
    //private final int VIEW_TYPE_FORM_LOGO        = 19;
    //private final int VIEW_TYPE_FORM_SNO         = 20;


    public static final String VIEW_TYPE_RADIO_GROUP_STR = "radio-group";
    public static final String VIEW_TYPE_SELECT_STR = "select";
    public static final String VIEW_TYPE_TEXT_STR = "text";
    public static final String VIEW_TYPE_DATE_STR = "date";
    public static final String VIEW_TYPE_RATINGS_STR = "starRating";
    public static final String VIEW_TYPE_SCORE_STR = "scoreRating";
    public static final String VIEW_TYPE_CAMERA_STR = "cameraUploader";
    public static final String VIEW_TYPE_VIDEO_STR = "videoUploader";
    public static final String VIEW_TYPE_TEXT_AREA_STR = "textarea";
    public static final String VIEW_TYPE_NUMBER_STR = "number";
    public static final String VIEW_TYPE_HEADER_STR = "header";
    public static final String VIEW_TYPE_CHECKBOX_GROUP_STR = "checkbox-group";
    public static final String VIEW_TYPE_FILE_STR = "file";
    public static final String VIEW_TYPE_AUDIO_STR = "audioUploader";
    public static final String VIEW_TYPE_PARAGRAPH_STR = "paragraph";
    public static final String VIEW_TYPE_THUMSUP_UPLOADER_STR = "thumsupUploader";
    public static final String VIEW_TYPE_SMILEY_UPLOADER_STR = "smileyUploader";
    public static final String VIEW_TYPE_BAR_CODE_STR         = "barcodeReader";

    //public static final String VIEW_TYPE_FORM_BG_COLOR_STR    = "formbg_color";
    //public static final String VIEW_TYPE_FORM_LOGO_STR        = "form_logo";
    //public static final String VIEW_TYPE_FORM_SNO_STR         = "form_sno";

    private static boolean isSNo = false;
    private static final int ColorGreen = Color.parseColor("#7ED321");
    private static final int ColorWhite = Color.parseColor("#FFFFFFFF");

//------------------------------------------------------- Constructor ----------------------------------------------------------------------------------------------------------------------

    public AdapterSurveyMarker(Context activity, List<FormDetailData> viewTypes ) {
        this.activity = activity;
        this.viewTypes = viewTypes;

        isSNo = true;//Utility.getBooleanSavedData(activity,Utility.SURVEY_FORM_SNO);

    }

//------------------------------------------------------- Get item View Type ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {

        if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_RADIO_GROUP_STR))
            return VIEW_TYPE_RADIO_GROUP;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_CHECKBOX_GROUP_STR))
            return VIEW_TYPE_CHECKBOX_GROUP;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_SELECT_STR))
            return VIEW_TYPE_SELECT;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_TEXT_STR))
            return VIEW_TYPE_TEXT;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_DATE_STR))
            return VIEW_TYPE_DATE;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_RATINGS_STR))
            return VIEW_TYPE_RATINGS;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_SCORE_STR))
            return VIEW_TYPE_SCORE;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_CAMERA_STR))
            return VIEW_TYPE_CAMERA;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_VIDEO_STR))
            return VIEW_TYPE_VIDEO;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_FILE_STR))
            return VIEW_TYPE_FILE;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_TEXT_AREA_STR))
            return VIEW_TYPE_TEXT_AREA;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_NUMBER_STR))
            return VIEW_TYPE_NUMBER;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_HEADER_STR))
            return VIEW_TYPE_HEADER;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_AUDIO_STR))
            return VIEW_TYPE_AUDIO;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_PARAGRAPH_STR))
            return VIEW_TYPE_PARAGRAPH;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_THUMSUP_UPLOADER_STR))
            return VIEW_TYPE_THUMSUP_UPLOADER;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_SMILEY_UPLOADER_STR))
            return VIEW_TYPE_SMILEY_UPLOADER;
        else if (viewTypes.get(position).getType().equalsIgnoreCase(VIEW_TYPE_BAR_CODE_STR))
            return VIEW_TYPE_BAR_CODE;

        return 0;
    }

//------------------------------------------------------- on Create View Holder ----------------------------------------------------------------------------------------------------------------------

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_RADIO_GROUP:
                return new AdapterSurveyMarker.RadioGroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_radio_group, parent, false));
            case VIEW_TYPE_CHECKBOX_GROUP:
                return new AdapterSurveyMarker.CheckboxGroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_checkbox_group, parent, false));
            case VIEW_TYPE_SELECT:
                return new AdapterSurveyMarker.SelectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_text_selection_view, parent, false));
            case VIEW_TYPE_TEXT:
                return new AdapterSurveyMarker.EditTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_view, parent, false));
            case VIEW_TYPE_TEXT_AREA:
                return new AdapterSurveyMarker.EditTextAreaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_area_view, parent, false));
            case VIEW_TYPE_NUMBER:
                return new AdapterSurveyMarker.EditTextNumberHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_number_type,parent, false));
            case VIEW_TYPE_DATE:
                return new AdapterSurveyMarker.DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_datetime_view, parent, false));
            case VIEW_TYPE_HEADER:
                return new AdapterSurveyMarker.HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_header_view, parent, false));
            case VIEW_TYPE_RATINGS:
                return new AdapterSurveyMarker.RatingsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_ratings, parent, false));
            case VIEW_TYPE_SCORE:
                return new AdapterSurveyMarker.ScoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_score_bar, parent, false));
            case VIEW_TYPE_CAMERA:
                return new AdapterSurveyMarker.CameraHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_camera_view, parent, false));
            case VIEW_TYPE_VIDEO:
                return new AdapterSurveyMarker.VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_video_view, parent, false));
            case VIEW_TYPE_FILE:
                return new AdapterSurveyMarker.FileHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_file_view, parent, false));
            case VIEW_TYPE_AUDIO:
                return new AdapterSurveyMarker.AudioHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_audio_view, parent, false));
            case VIEW_TYPE_PARAGRAPH:
                return new AdapterSurveyMarker.ParagraphHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_paragraph_view, parent, false));
            case VIEW_TYPE_THUMSUP_UPLOADER:
                return new AdapterSurveyMarker.THUMPSUP_UPLOADER_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_thumsup_view, parent, false));
            case VIEW_TYPE_SMILEY_UPLOADER:
                return new AdapterSurveyMarker.SMILEY_UPLOADER_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_smiley_view, parent, false));
            case VIEW_TYPE_BAR_CODE:
                return new AdapterSurveyMarker.BarCode_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_barcode_view, parent, false));
            default:
                return new AdapterSurveyMarker.TempHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false));
        }
    }

//------------------------------------------------------- on Bind View Holder ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FormDetailData bin = viewTypes.get(position);

        switch (getItemViewType(position)) {


//------------------------------------------------------- Text  ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_TEXT:
                if (holder instanceof AdapterSurveyMarker.EditTextHolder) {
                    AdapterSurveyMarker.EditTextHolder textViewHolder = ((AdapterSurveyMarker.EditTextHolder) holder);
                    textViewHolder.txtQuestion.setText(bin.getLabel());
                    textViewHolder.edtDescription.setEnabled(false);
                    textViewHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());

                    if(isSNo){
                        textViewHolder.txtSNoEditTextView.setVisibility(View.VISIBLE);
                        textViewHolder.txtSNoEditTextView.setText((position+1)+".");
                    }
                    else{
                        textViewHolder.txtSNoEditTextView.setVisibility(View.GONE);
                        textViewHolder.txtSNoEditTextView.setText("");
                    }
                }
                break;

//------------------------------------------------------- Text Area ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_TEXT_AREA:
                if (holder instanceof AdapterSurveyMarker.EditTextAreaHolder) {
                    AdapterSurveyMarker.EditTextAreaHolder textViewHolder = ((AdapterSurveyMarker.EditTextAreaHolder) holder);
                    textViewHolder.txtQuestion.setText(bin.getLabel());
                    textViewHolder.edtDescription.setEnabled(false);
                    textViewHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());

                    if(isSNo){
                        textViewHolder.txtSNoEditTextArea.setVisibility(View.VISIBLE);
                        textViewHolder.txtSNoEditTextArea.setText((position+1)+".");
                    }
                    else{
                        textViewHolder.txtSNoEditTextArea.setVisibility(View.GONE);
                        textViewHolder.txtSNoEditTextArea.setText("");
                    }
                }

                break;

//------------------------------------------------------- Number ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_NUMBER:
                if (holder instanceof AdapterSurveyMarker.EditTextNumberHolder) {
                    AdapterSurveyMarker.EditTextNumberHolder textViewHolder = ((AdapterSurveyMarker.EditTextNumberHolder) holder);
                    textViewHolder.txtQuestion.setText(bin.getLabel());
                    textViewHolder.edtDescription.setEnabled(false);
                    textViewHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());

                    if(isSNo){
                        textViewHolder.txtSNoEditTextNumber.setVisibility(View.VISIBLE);
                        textViewHolder.txtSNoEditTextNumber.setText((position+1)+".");
                    }
                    else{
                        textViewHolder.txtSNoEditTextNumber.setVisibility(View.GONE);
                        textViewHolder.txtSNoEditTextNumber.setText("");
                    }

                }
                break;

//------------------------------------------------------- Date ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_DATE:
                if (holder instanceof AdapterSurveyMarker.DateHolder) {
                    AdapterSurveyMarker.DateHolder dateHolder = ((AdapterSurveyMarker.DateHolder) holder);
                    dateHolder.txtQuestion.setText(bin.getLabel());
                    dateHolder.tvdatetime.setEnabled(false);
                    dateHolder.tvdatetime.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());
                    if(isSNo){
                        dateHolder.txtSNoDateTime.setVisibility(View.VISIBLE);
                        dateHolder.txtSNoDateTime.setText((position+1)+".");
                    }
                    else{
                        dateHolder.txtSNoDateTime.setVisibility(View.GONE);
                        dateHolder.txtSNoDateTime.setText("");
                    }

                }
                break;

//------------------------------------------------------- Radio Button ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_RADIO_GROUP:
                if (holder instanceof AdapterSurveyMarker.RadioGroupHolder) {
                    AdapterSurveyMarker.RadioGroupHolder radioGroupHolder = ((AdapterSurveyMarker.RadioGroupHolder) holder);
                    radioGroupHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        radioGroupHolder.txtSNoRadioGroup.setVisibility(View.VISIBLE);
                        radioGroupHolder.txtSNoRadioGroup.setText((position+1)+".");
                    }
                    else{
                        radioGroupHolder.txtSNoRadioGroup.setVisibility(View.GONE);
                        radioGroupHolder.txtSNoRadioGroup.setText("");
                    }

                    radioGroupHolder.llRadioGroupLayout.setTag(position);
                    int id = (position + 1) * 100;
                    radioGroupHolder.llRadioGroupLayout.removeAllViews();
                    for (int i = 0; i < bin.getSelvalues().size(); i++) {
                        FormDetailData.FormOptions binFormDetailOps = bin.getSelvalues().get(i);
                        View viewRadioButton = LayoutInflater.from(activity).inflate(R.layout.custom_radio_btn, null);
                        viewRadioButton.setId(id);
                        id++;
                        radioGroupHolder.llRadioGroupLayout.addView(viewRadioButton);
                        ((RadioButton) viewRadioButton).setText(binFormDetailOps.getLabel());
                        ((RadioButton) viewRadioButton).setChecked(binFormDetailOps.isSelected());
                        viewRadioButton.setTag(radioGroupHolder);
                        viewRadioButton.setEnabled(false);
                    }
                }
                break;

//------------------------------------------------------- CheckBox ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_CHECKBOX_GROUP:
                if (holder instanceof AdapterSurveyMarker.CheckboxGroupHolder) {
                    AdapterSurveyMarker.CheckboxGroupHolder checkboxGroupHolder = ((AdapterSurveyMarker.CheckboxGroupHolder) holder);
                    checkboxGroupHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        checkboxGroupHolder.txtSNoCheckGroup.setVisibility(View.VISIBLE);
                        checkboxGroupHolder.txtSNoCheckGroup.setText((position+1)+".");
                    }
                    else{
                        checkboxGroupHolder.txtSNoCheckGroup.setVisibility(View.GONE);
                        checkboxGroupHolder.txtSNoCheckGroup.setText("");
                    }

                    checkboxGroupHolder.llCheckBoxGroupLayout.setTag(position);
                    int id = (position + 1) * 100;
                    checkboxGroupHolder.llCheckBoxGroupLayout.removeAllViews();

                    if (bin.getSelvalues() != null) {
                        for (int i = 0; i < bin.getSelvalues().size(); i++) {
                            FormDetailData.FormOptions binFormDetailOps = bin.getSelvalues().get(i);
                            View customCheckBoxLayout = LayoutInflater.from(activity).inflate(R.layout.custom_chkbox_btn, null);
                            customCheckBoxLayout.setId(id);
                            id++;
                            checkboxGroupHolder.llCheckBoxGroupLayout.addView(customCheckBoxLayout);
                            CheckBox checkBox = customCheckBoxLayout.findViewById(R.id.checkBox);
                            checkBox.setText(binFormDetailOps.getLabel());
                            checkBox.setChecked(binFormDetailOps.isSelected());
                            checkBox.setTag(i);
                            checkBox.setEnabled(false);
                        }
                    }
                }
                break;

//-------------------------------------------------------  Select ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SELECT:
                if (holder instanceof AdapterSurveyMarker.SelectHolder) {
                    AdapterSurveyMarker.SelectHolder selectHolder = ((AdapterSurveyMarker.SelectHolder) holder);
                    String selectedText = "";
                    for (FormDetailData.FormOptions binOp : bin.getSelvalues()) {
                        if (binOp.isSelected()) {
                            selectedText = binOp.getLabel();
                            break;
                        }
                    }

                    if(isSNo){
                        selectHolder.txtSNoSelect.setVisibility(View.VISIBLE);
                        selectHolder.txtSNoSelect.setText((position+1)+".");
                    }
                    else{
                        selectHolder.txtSNoSelect.setVisibility(View.GONE);
                        selectHolder.txtSNoSelect.setText("");
                    }

                    selectHolder.selectArrow.setVisibility(View.GONE);
                    selectHolder.txtQuestion.setText(bin.getLabel());
                    //selectHolder.tvDescription.setText(Utility.isEmptyString(selectedText) ? activity.getString(R.string.select) : selectedText);
                    selectHolder.tvName.setText(Utility.isEmptyString(selectedText) ? activity.getString(R.string.select) : selectedText);
                }
                break;

//------------------------------------------------------- Rating ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_RATINGS:
                if (holder instanceof AdapterSurveyMarker.RatingsHolder) {
                    AdapterSurveyMarker.RatingsHolder ratingsHolder = ((AdapterSurveyMarker.RatingsHolder) holder);
                    ratingsHolder.txtQuestion.setText(bin.getLabel());
                    ratingsHolder.ratingBar.setRating(Utility.isEmptyString(bin.getFill_value()) ? 0 : Float.parseFloat(bin.getFill_value()));
                    ratingsHolder.ratingBar.setEnabled(false);

                    if(isSNo){
                        ratingsHolder.txtSNoRating.setVisibility(View.VISIBLE);
                        ratingsHolder.txtSNoRating.setText((position+1)+".");
                    }
                    else{
                        ratingsHolder.txtSNoRating.setVisibility(View.GONE);
                        ratingsHolder.txtSNoRating.setText("");
                    }

                }
                break;

//------------------------------------------------------- Score ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SCORE:
                if (holder instanceof AdapterSurveyMarker.ScoreHolder) {
                    AdapterSurveyMarker.ScoreHolder scoreHolder = (AdapterSurveyMarker.ScoreHolder) holder;
                    scoreHolder.txtQuestion.setText(bin.getLabel());
                    scoreHolder.llScoreViewParent.setTag(position);

                    if (isSNo) {
                        scoreHolder.txtSNoScoreBar.setVisibility(View.VISIBLE);
                        scoreHolder.txtSNoScoreBar.setText((position + 1) + ".");
                    } else {
                        scoreHolder.txtSNoScoreBar.setVisibility(View.GONE);
                        scoreHolder.txtSNoScoreBar.setText("");
                    }

                    ArrayList<TextView> listTxtViews = new ArrayList<>();
                    listTxtViews.add(scoreHolder.txtScore1);
                    listTxtViews.add(scoreHolder.txtScore2);
                    listTxtViews.add(scoreHolder.txtScore3);
                    listTxtViews.add(scoreHolder.txtScore4);
                    listTxtViews.add(scoreHolder.txtScore5);
                    listTxtViews.add(scoreHolder.txtScore6);
                    listTxtViews.add(scoreHolder.txtScore7);
                    listTxtViews.add(scoreHolder.txtScore8);
                    listTxtViews.add(scoreHolder.txtScore9);
                    listTxtViews.add(scoreHolder.txtScore10);

                    if (Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("0");
                    }
                    else{

                        for (int i = 1; i <= listTxtViews.size(); i++) {
                            listTxtViews.get(i - 1).setSelected(i <= Integer.parseInt(bin.getFill_value()));
//                        listTxtViews.get(i - 1).setTag(i);
//                        listTxtViews.get(i - 1).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                 bin.setFill_value("" + (Integer) view.getTag());
//                                for (int i = 1; i <= listTxtViews.size(); i++) {
//                                     listTxtViews.get(i - 1).setSelected(i <= Integer.parseInt(bin.getFill_value()));
//                                }
//                                notifyDataSetChanged();
//                            }
//                        });
                        }

                    }


                }
                break;

//------------------------------------------------------- Camera/Image ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_CAMERA: // VIEW_TYPE_IMAGE
                if (holder instanceof AdapterSurveyMarker.CameraHolder) {
                    AdapterSurveyMarker.CameraHolder cameraHolder = ((AdapterSurveyMarker.CameraHolder) holder);
                    cameraHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        cameraHolder.txtSNoCamera.setVisibility(View.VISIBLE);
                        cameraHolder.txtSNoCamera.setText((position+1)+".");
                    }
                    else{
                        cameraHolder.txtSNoCamera.setVisibility(View.GONE);
                        cameraHolder.txtSNoCamera.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        bin.setFill_value("");
                        cameraHolder.imgCaptured.setImageResource(R.drawable.icon_no_image);
                    }
                    else{
                        String imagePath = bin.getFill_value().split(",")[0].split("#")[1];
                        if(bin.getFill_value().split(",")[0].split("#")[0].startsWith("local")){
                            Glide.with(activity).load(imagePath).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(cameraHolder.imgCaptured);
                        }
                        else{
                            Uri uri = Uri.parse(imagePath);
                            Glide.with(activity).load(uri).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(cameraHolder.imgCaptured);
                        }

                        cameraHolder.imgCaptured.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.image_zoom_view_layout);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                ImageView imageView = dialog.findViewById(R.id.dialogbox_image);
                                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
                                // Image Load!
                                if(bin.getFill_value().split(",")[0].split("#")[0].startsWith("local")){
                                    Glide.with(activity).load(imagePath).error(R.drawable.icon_no_image).into(imageView);
                                }
                                else{
                                    Uri uri = Uri.parse(imagePath);
                                    Glide.with(activity).load(uri).error(R.drawable.icon_no_image).into(imageView);
                                }
                                photoViewAttacher.update();
                                dialog.show();
                            }
                        });


                    }
                    cameraHolder.imgCaptured.setTag(position);

                }
                break;

//------------------------------------------------------- Video ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_VIDEO:
                if (holder instanceof AdapterSurveyMarker.VideoHolder) {
                    AdapterSurveyMarker.VideoHolder videoHolder = (AdapterSurveyMarker.VideoHolder) holder;
                    videoHolder.txtQuestion.setText(bin.getLabel());
                    videoHolder.imgVideo.setText("View");
                    videoHolder.imgVideo.setTag(position);
                    ArrayList<FileUploadViewModel> videoUploadList = new ArrayList<>();


                    if(isSNo){
                        videoHolder.txtSNoVideo.setVisibility(View.VISIBLE);
                        videoHolder.txtSNoVideo.setText((position+1)+".");
                    }
                    else{
                        videoHolder.txtSNoVideo.setVisibility(View.GONE);
                        videoHolder.txtSNoVideo.setText("");
                    }
                    if (Utility.isEmptyString(bin.getFill_value())) {
                       videoHolder.tv_videoUploadName.setText("No Video Found");
                    }
                    else
                    {
                       videoHolder.tv_videoUploadName.setText("Video Found");
                        int n = bin.getFill_value().split(",").length;

                        for(int i=0; i<n; i++){
                            if (bin.getFill_value().split(",")[i].split("#")[0].startsWith("local%")) {
                                String filePath = bin.getFill_value().split(",")[i].split("#")[1];
                                File file = new File(filePath);
                                String fileName = file.getName();
                                videoUploadList.add(new FileUploadViewModel(fileName, filePath, false));
                            }
                            else {
                                String filename = bin.getFill_value().split(",")[i].split("#")[0];
                                String filepath = bin.getFill_value().split(",")[i].split("#")[1];
                                videoUploadList.add(new FileUploadViewModel(filename, filepath, true));
                            }
                        }

                    }
                    videoHolder.imgVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (Utility.isEmptyString(bin.getFill_value())) {
                                Toast.makeText(activity, "No Video Found", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ViewFileUploadDialogBox(videoUploadList);
                            }
                        }
                    });
                }
                break;

//------------------------------------------------------- File ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_FILE:

                if (holder instanceof AdapterSurveyMarker.FileHolder) {
                    AdapterSurveyMarker.FileHolder fileHolder = ((AdapterSurveyMarker.FileHolder) holder);
                    fileHolder.txtQuestion.setText(bin.getLabel());
                    fileHolder.rlFileCapture.setText("View");
                    fileHolder.rlFileCapture.setTag(position);
                    ArrayList<FileUploadViewModel> fileUploadList = new ArrayList<>();


                    if(isSNo){
                        fileHolder.txtSNoFile.setVisibility(View.VISIBLE);
                        fileHolder.txtSNoFile.setText((position+1)+".");
                    }
                    else{
                        fileHolder.txtSNoFile.setVisibility(View.GONE);
                        fileHolder.txtSNoFile.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        fileHolder.tv_fileUploadName.setText("No File Found");
                    }
                    else
                    {
                        fileHolder.tv_fileUploadName.setText("File Found");
                        int n = bin.getFill_value().split(",").length;
                        for(int i=0; i<n; i++) {

                            if(bin.getFill_value().split(",")[i].split("#")[0].startsWith("local%")) {
                                    String filePath = bin.getFill_value().split(",")[i].split("#")[1];
                                    File file = new File(filePath);
                                    String fileName = file.getName();
                                    fileUploadList.add(new FileUploadViewModel(fileName, filePath, false));
                            }
                            else {
                                String filename = bin.getFill_value().split(",")[i].split("#")[0];
                                String filepath = bin.getFill_value().split(",")[i].split("#")[1];
                                fileUploadList.add(new FileUploadViewModel(filename, filepath, true));
                            }
                        }

                    }

                    fileHolder.rlFileCapture.setOnClickListener(view -> {

                        if(Utility.isEmptyString(bin.getFill_value())){
                             Toast.makeText(activity, "No File Found", Toast.LENGTH_SHORT).show();
                        }
                        else{
                             ViewFileUploadDialogBox(fileUploadList);
                        }
                    });

                }
                break;

//------------------------------------------------------- Audio ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_AUDIO:
                if (holder instanceof AdapterSurveyMarker.AudioHolder) {
                    AdapterSurveyMarker.AudioHolder audioHolder = ((AdapterSurveyMarker.AudioHolder) holder);
                    audioHolder.txtQuestion.setText(bin.getLabel());
                    audioHolder.rlFileCaptureAudio.setText("View");
                    audioHolder.rlFileCaptureAudio.setTag(position);
                    ArrayList<FileUploadViewModel> audioUploadList = new ArrayList<>();

                    if(isSNo){
                        audioHolder.txtSNoAudio.setVisibility(View.VISIBLE);
                        audioHolder.txtSNoAudio.setText((position+1)+".");
                    }
                    else{
                        audioHolder.txtSNoAudio.setVisibility(View.GONE);
                        audioHolder.txtSNoAudio.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        audioHolder.tv_audioUploadName.setText("No Audio Found");
                    }
                    else
                    {
                        audioHolder.tv_audioUploadName.setText("Audio Found");
                        int n = bin.getFill_value().split(",").length;
                        for(int i=0; i<n; i++){
                            if (bin.getFill_value().split(",")[i].split("#")[0].startsWith("local%")) {
                                String filePath = bin.getFill_value().split(",")[i].split("#")[1];
                                File file = new File(filePath);
                                String fileName = file.getName();
                                audioUploadList.add(new FileUploadViewModel(fileName, filePath, false));
                            }
                            else {
                                String filename = bin.getFill_value().split(",")[i].split("#")[0];
                                String filepath = bin.getFill_value().split(",")[i].split("#")[1];
                                audioUploadList.add(new FileUploadViewModel(filename, filepath, true));
                            }
                        }

                    }

                    audioHolder.rlFileCaptureAudio.setOnClickListener(view -> {
                        if (Utility.isEmptyString(bin.getFill_value())) {
                            Toast.makeText(activity, "No Audio Found", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ViewFileUploadDialogBox(audioUploadList);
                        }
                    });

                }
                break;

//------------------------------------------------------- Thums Up ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_THUMSUP_UPLOADER:
                if (holder instanceof AdapterSurveyMarker.THUMPSUP_UPLOADER_Holder) {
                    AdapterSurveyMarker.THUMPSUP_UPLOADER_Holder thumpsup_uploader_holder = ((AdapterSurveyMarker.THUMPSUP_UPLOADER_Holder) holder);
                    thumpsup_uploader_holder.thumsup_textview.setText(bin.getLabel());

                    if(isSNo){
                        thumpsup_uploader_holder.txtSNoThumsup.setVisibility(View.VISIBLE);
                        thumpsup_uploader_holder.txtSNoThumsup.setText((position+1)+".");
                    }
                    else{
                        thumpsup_uploader_holder.txtSNoThumsup.setVisibility(View.GONE);
                        thumpsup_uploader_holder.txtSNoThumsup.setText("");
                    }

                    // When user not select any Item
                    if(Utility.isEmptyString(bin.getFill_value())){
                        thumpsup_uploader_holder.thums_up_imageview.setImageResource(R.drawable.thumbs_up);
                        thumpsup_uploader_holder.thums_down_imageview.setImageResource(R.drawable.thumbs_down);
                    }
                    else{
                        if(bin.getFill_value().equals("up")){
                            thumpsup_uploader_holder.thums_up_imageview.setImageResource(R.drawable.thumbs_up_fill);
                            thumpsup_uploader_holder.thums_down_imageview.setImageResource(R.drawable.thumbs_down);
                        }
                        if(bin.getFill_value().equals("down")){
                            thumpsup_uploader_holder.thums_down_imageview.setImageResource(R.drawable.thumbs_down_fill);
                            thumpsup_uploader_holder.thums_up_imageview.setImageResource(R.drawable.thumbs_up);
                        }
                    }


                }
                break;

//------------------------------------------------------- Smiley ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SMILEY_UPLOADER:
                if (holder instanceof AdapterSurveyMarker.SMILEY_UPLOADER_Holder) {
                    AdapterSurveyMarker.SMILEY_UPLOADER_Holder smiley_holder = ((AdapterSurveyMarker.SMILEY_UPLOADER_Holder) holder);
                    smiley_holder.smiley_textview.setText(bin.getLabel());

                    if(isSNo){
                        smiley_holder.txtSNoSmiley.setVisibility(View.VISIBLE);
                        smiley_holder.txtSNoSmiley.setText((position+1)+".");
                    }
                    else{
                        smiley_holder.txtSNoSmiley.setVisibility(View.GONE);
                        smiley_holder.txtSNoSmiley.setText("");
                    }

                    if(Utility.isEmptyString(bin.getFill_value())){
                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    }
                    else{
                        switch (bin.getFill_value()){

                            case "1":
                                smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.green));
                                smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                break;

                            case "2":
                                smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.green));
                                smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                break;

                            case "3":
                                smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.green));
                                smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                break;

                            case "4":
                                smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.green));
                                smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                break;

                            case "5":
                                smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
                                smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.green));
                                break;
                        }
                    }

                }
                break;

//------------------------------------------------------- ParaGraph ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_PARAGRAPH:
                if (holder instanceof AdapterSurveyMarker.ParagraphHolder) {
                    AdapterSurveyMarker.ParagraphHolder paragraphHolder = ((AdapterSurveyMarker.ParagraphHolder) holder);
                    paragraphHolder.txt_paragraph.setText(bin.getLabel());
                    bin.setFill_value(bin.getLabel());
                }
                break;


//------------------------------------------------------- Bar Code ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_BAR_CODE:
                if (holder instanceof AdapterSurveyMarker.BarCode_Holder) {
                    AdapterSurveyMarker.BarCode_Holder barCode_holder= ((AdapterSurveyMarker.BarCode_Holder) holder);
                    barCode_holder.barcode_textview.setText(bin.getLabel());

                    if(isSNo){
                        barCode_holder.txtSNoBarCode.setVisibility(View.VISIBLE);
                        barCode_holder.txtSNoBarCode.setText((position+1)+".");
                    }
                    else{
                        barCode_holder.txtSNoBarCode.setVisibility(View.GONE);
                        barCode_holder.txtSNoBarCode.setText("");
                    }
                    if(Utility.isEmptyString(bin.getFill_value())){
                        barCode_holder.barcodeResult.setHint("Result not found");
                    }
                    else{
                        barCode_holder.barcodeResult.setText(bin.getFill_value());
                    }


                }
                break;


//------------------------------------------------------- Header ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_HEADER:
                if (holder instanceof AdapterSurveyMarker.HeaderHolder) {
                    AdapterSurveyMarker.HeaderHolder editTextHolder = ((AdapterSurveyMarker.HeaderHolder) holder);
                    editTextHolder.txtQuestion.setText(bin.getLabel());
                    bin.setFill_value(bin.getLabel());
                }
                break;

//------------------------------------------------------- Default ----------------------------------------------------------------------------------------------------------------------

            default:
                if (holder instanceof AdapterSurveyMarker.TempHolder) {
                    AdapterSurveyMarker.TempHolder tempHolder = ((AdapterSurveyMarker.TempHolder) holder);
                    tempHolder.txtSpinner.setText("TEMP HOLDER");
                }
                break;
        }
    }

//------------------------------------------------------- Get Item Count ----------------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        return viewTypes.size();
    }

//------------------------------------------------------- View Holder ----------------------------------------------------------------------------------------------------------------------

    private static class RadioGroupHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion,txtSNoRadioGroup;
        LinearLayout llRadioGroupLayout;
        ImageView error_imageview;
        RadioGroupHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            llRadioGroupLayout = itemView.findViewById(R.id.llRadioGroupLayout);
            error_imageview = itemView.findViewById(R.id.rb_error_imageview);
            txtSNoRadioGroup = itemView.findViewById(R.id.txtSNoRadioGroup);

        }
    }

    private static class CheckboxGroupHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion,txtSNoCheckGroup;
        LinearLayout llCheckBoxGroupLayout;
        ImageView error_imageview;

        CheckboxGroupHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            llCheckBoxGroupLayout = itemView.findViewById(R.id.llCheckBoxGroupLayout);
            error_imageview = itemView.findViewById(R.id.cb_error_imageview);
            txtSNoCheckGroup = itemView.findViewById(R.id.txtSNoCheckGroup);

        }
    }

    private static class SelectHolder extends RecyclerView.ViewHolder {

        TextView tvName, txtQuestion,txtSNoSelect;
        RelativeLayout rlSelect;
        ImageView error_imageview,selectArrow;
        SelectHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            tvName = itemView.findViewById(R.id.tvName);
            rlSelect = itemView.findViewById(R.id.rlSelect);
            error_imageview = itemView.findViewById(R.id.select_error_imageview);
            selectArrow = itemView.findViewById(R.id.selectArrow);
            txtSNoSelect = itemView.findViewById(R.id.txtSNoSelect);
        }

//        TextView txtQuestion;
//        TextView tvDescription;
//        ImageView error_imageview;
//
//        SelectHolder(@NonNull View itemView) {
//            super(itemView);
//            txtQuestion = itemView.findViewById(R.id.txtQuestion);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
//        }

    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion;
        TextView tvDescription;
        ImageView error_imageview;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
        }
    }

    private static class EditTextHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtSNoEditTextView;
        EditText edtDescription;
        ImageView error_imageview;
        EditTextHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            edtDescription = itemView.findViewById(R.id.edtDescriptiontextview);
            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
            txtSNoEditTextView = itemView.findViewById(R.id.txtSNoEditTextView);

        }
//        TextView txtQuestion;
//        TextView tvDescription;
//        ImageView error_imageview;
//
//        EditTextHolder(@NonNull View itemView) {
//            super(itemView);
//            txtQuestion = itemView.findViewById(R.id.txtQuestion);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
//        }
    }

    private static class EditTextAreaHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtSNoEditTextArea;
        EditText edtDescription;
        ImageView error_imageview;
        EditTextAreaHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            edtDescription = itemView.findViewById(R.id.edtDescriptiontextarea);
            error_imageview = itemView.findViewById(R.id.eda_error_imageview);
            txtSNoEditTextArea = itemView.findViewById(R.id.txtSNoEditTextArea);

        }
//        TextView txtQuestion;
//        TextView tvDescription;
//        ImageView error_imageview;
//
//        EditTextAreaHolder(@NonNull View itemView) {
//            super(itemView);
//            txtQuestion = itemView.findViewById(R.id.txtQuestion);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
//        }
    }

    private static class EditTextNumberHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtSNoEditTextNumber;
        EditText edtDescription;
        ImageView error_imageview;
        EditTextNumberHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            edtDescription = itemView.findViewById(R.id.edtDescriptionnumber);
            error_imageview = itemView.findViewById(R.id.edn_error_imageview);
            txtSNoEditTextNumber = itemView.findViewById(R.id.txtSNoEditTextNumber);

        }

//        TextView txtQuestion;
//        TextView tvDescription;
//        ImageView error_imageview;
//
//        EditTextNumberHolder(@NonNull View itemView) {
//            super(itemView);
//            txtQuestion = itemView.findViewById(R.id.txtQuestion);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
//        }

    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion;

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
        }
    }

    private static class DateHolder extends RecyclerView.ViewHolder {

        TextView tvdatetime, txtQuestion,txtSNoDateTime;
        RelativeLayout rlSelect;
        ImageView error_imageview;
        DateHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            tvdatetime = itemView.findViewById(R.id.tvdatetime);
            rlSelect = itemView.findViewById(R.id.rlSelect);
            error_imageview = itemView.findViewById(R.id.date_error_imageview);
            txtSNoDateTime = itemView.findViewById(R.id.txtSNoDateTime);

        }


//        TextView txtQuestion;
//        TextView tvDescription;
//        ImageView error_imageview;
//
//        DateHolder(@NonNull View itemView) {
//            super(itemView);
//            txtQuestion = itemView.findViewById(R.id.txtQuestion);
//            tvDescription = itemView.findViewById(R.id.tvDescription);
//            error_imageview = itemView.findViewById(R.id.ed_error_imageview);
//        }
    }

    private static class RatingsHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtSNoRating;
        RatingBar ratingBar;
        ImageView error_imageview;
        RatingsHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            error_imageview = itemView.findViewById(R.id.rating_error_imageview);
            txtSNoRating = itemView.findViewById(R.id.txtSNoRating);
        }
    }

    private static class ScoreHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion, txtScore1, txtScore2, txtScore3, txtScore4,
                txtScore5, txtScore6, txtScore7, txtScore8, txtScore9, txtScore10,txtSNoScoreBar;
        LinearLayout llScoreViewParent;

        ImageView error_imageview;

        ScoreHolder(@NonNull View itemView) {
            super(itemView);
            llScoreViewParent = itemView.findViewById(R.id.llScoreViewParent);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            txtScore1 = itemView.findViewById(R.id.txtScore1);
            txtScore2 = itemView.findViewById(R.id.txtScore2);
            txtScore3 = itemView.findViewById(R.id.txtScore3);
            txtScore4 = itemView.findViewById(R.id.txtScore4);
            txtScore5 = itemView.findViewById(R.id.txtScore5);
            txtScore6 = itemView.findViewById(R.id.txtScore6);
            txtScore7 = itemView.findViewById(R.id.txtScore7);
            txtScore8 = itemView.findViewById(R.id.txtScore8);
            txtScore9 = itemView.findViewById(R.id.txtScore9);
            txtScore10 = itemView.findViewById(R.id.txtScore10);
            error_imageview = itemView.findViewById(R.id.score_error_imageview);
            txtSNoScoreBar = itemView.findViewById(R.id.txtSNoScoreBar);

        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder {

        //TextView txtQuestion;
        //ImageView imgCaptured;
        //ImageView error_imageview;

        ImageHolder(@NonNull View itemView) {
            super(itemView);
            // txtQuestion = itemView.findViewById(R.id.txtQuestion);
            // imgCaptured = itemView.findViewById(R.id.imgCaptured);
            // error_imageview = itemView.findViewById(R.id.image_error_imageview);
        }
    }
    private static class CameraHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,txtSNoCamera;
        ImageView imgCaptured;
        ImageView error_imageview;

        CameraHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            imgCaptured = itemView.findViewById(R.id.imgCaptured);
            error_imageview = itemView.findViewById(R.id.image_error_imageview);
            txtSNoCamera = itemView.findViewById(R.id.txtSNoCamera);
        }
    }
    private static class VideoHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,tv_videoUploadName,txtSNoVideo;
        //ImageView imgVideo;
        Button imgVideo;
        LinearLayout llImageVideo;
        ImageView error_imageview;

        VideoHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            imgVideo = itemView.findViewById(R.id.imgVideo);
            error_imageview = itemView.findViewById(R.id.video_error_imageview);
            llImageVideo = itemView.findViewById(R.id.llImageVideo);
            tv_videoUploadName = itemView.findViewById(R.id.tv_videoUploadName);
            txtSNoVideo = itemView.findViewById(R.id.txtSNoVideo);
        }
    }

    private static class FileHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,tv_fileUploadName,txtSNoFile;
        ImageView imgFileTickMark;
        //RelativeLayout rlFileCapture;
        Button rlFileCapture;
        ImageView error_imageview;


        FileHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            imgFileTickMark = itemView.findViewById(R.id.imgFileTickMark);
            rlFileCapture = itemView.findViewById(R.id.imgFileCaptured);
            error_imageview = itemView.findViewById(R.id.file_error_imageview);
            tv_fileUploadName = itemView.findViewById(R.id.tv_fileUploadName);
            txtSNoFile = itemView.findViewById(R.id.txtSNoFile);
        }
    }

    private static class AudioHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion,tv_audioUploadName,txtSNoAudio;
        ImageView imgFileTickMarkAudio;
        //RelativeLayout rlFileCaptureAudio;
        Button rlFileCaptureAudio;
        ImageView error_imageview;


        AudioHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            imgFileTickMarkAudio = itemView.findViewById(R.id.imgAudioFileTickMark);
            rlFileCaptureAudio = itemView.findViewById(R.id.imgAudioFileCaptured);
            error_imageview = itemView.findViewById(R.id.audio_error_imageview);
            tv_audioUploadName = itemView.findViewById(R.id.tv_audioUploadName);
            txtSNoAudio = itemView.findViewById(R.id.txtSNoAudio);
        }
    }

    private static class TempHolder extends RecyclerView.ViewHolder {

        TextView txtSpinner;
        TempHolder(@NonNull View itemView) {
            super(itemView);
            txtSpinner = itemView.findViewById(R.id.text1);
        }
    }

    private static class ParagraphHolder extends RecyclerView.ViewHolder {

        TextView txt_paragraph;
        ParagraphHolder(@NonNull View itemView) {
            super(itemView);
            txt_paragraph = itemView.findViewById(R.id.txt_paragraph);
        }
    }

    private static class THUMPSUP_UPLOADER_Holder extends RecyclerView.ViewHolder {
        TextView thumsup_textview,txtSNoThumsup;
        ImageView thumsup_error_imageview,thums_up_imageview,thums_down_imageview;

        THUMPSUP_UPLOADER_Holder(@NonNull View itemView) {
            super(itemView);
            thumsup_textview = itemView.findViewById(R.id.thumsup_textview);
            thumsup_error_imageview = itemView.findViewById(R.id.thumsup_error_imageview);
            thums_up_imageview = itemView.findViewById(R.id.thums_up_imageview);
            thums_down_imageview = itemView.findViewById(R.id.thums_down_imageview);
            txtSNoThumsup = itemView.findViewById(R.id.txtSNoThumsup);
        }
    }

    private static class SMILEY_UPLOADER_Holder extends RecyclerView.ViewHolder {
        TextView smiley_textview,txtSNoSmiley;
        ImageView smiley_error_imageview,smiley_emoji_1,smiley_emoji_2,smiley_emoji_3,smiley_emoji_4,smiley_emoji_5;
        LinearLayout ll_Smiley1,ll_Smiley2,ll_Smiley3,ll_Smiley4,ll_Smiley5;

        SMILEY_UPLOADER_Holder(@NonNull View itemView) {
            super(itemView);
            smiley_textview = itemView.findViewById(R.id.smiley_textview);
            smiley_error_imageview = itemView.findViewById(R.id.smiley_error_imageview);
            smiley_emoji_1 = itemView.findViewById(R.id.smiley_emoji_1);
            smiley_emoji_2 = itemView.findViewById(R.id.smiley_emoji_2);
            smiley_emoji_3 = itemView.findViewById(R.id.smiley_emoji_3);
            smiley_emoji_4 = itemView.findViewById(R.id.smiley_emoji_4);
            smiley_emoji_5 = itemView.findViewById(R.id.smiley_emoji_5);
            ll_Smiley1     = itemView.findViewById(R.id.ll_Smiley1);
            ll_Smiley2     = itemView.findViewById(R.id.ll_Smiley2);
            ll_Smiley3     = itemView.findViewById(R.id.ll_Smiley3);
            ll_Smiley4     = itemView.findViewById(R.id.ll_Smiley4);
            ll_Smiley5     = itemView.findViewById(R.id.ll_Smiley5);
            txtSNoSmiley   = itemView.findViewById(R.id.txtSNoSmiley);
        }
    }


    private static class BarCode_Holder extends RecyclerView.ViewHolder {

        TextView txtSNoBarCode,barcode_textview,barcodeResult;
        ImageView barcode_error_imageview,barcodeCaptured;
        LinearLayout llBarCode;

        BarCode_Holder(@NonNull View itemView) {
            super(itemView);
            txtSNoBarCode           = itemView.findViewById(R.id.txtSNoBarCode);
            barcode_textview        = itemView.findViewById(R.id.barcode_textview);
            barcode_error_imageview = itemView.findViewById(R.id.barcode_error_imageview);
            barcodeCaptured         = itemView.findViewById(R.id.barcodeCaptured);
            llBarCode               = itemView.findViewById(R.id.llBarCode);
            barcodeResult           = itemView.findViewById(R.id.barcodeResult);
        }
    }



    private void ViewFileUploadDialogBox(ArrayList<FileUploadViewModel> fileUploadViewModelArrayList){
        // DialogBox
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_fileuploadview_layout_dialogbox);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // TextView
        TextView tvUploadName = dialog.findViewById(R.id.tvUploadName);
        tvUploadName.setText("File Upload View");
        // RecycleView
        RecyclerView recyclerView = dialog.findViewById(R.id.file_upload_view_recycle_view);
        // Cancel Button
        Button cancel_bt = dialog.findViewById(R.id.file_upload_view_cancel_bt);
        cancel_bt.setOnClickListener(view1 -> dialog.dismiss());
        // Adapter
        FileUploadViewAdapter fileUploadViewAdapter = new FileUploadViewAdapter(activity,fileUploadViewModelArrayList);
        // Set Adapter
        recyclerView.setAdapter(fileUploadViewAdapter);
        // Set Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        fileUploadViewAdapter.notifyDataSetChanged();
        // Dialog box Show
        dialog.show();
    }


}
