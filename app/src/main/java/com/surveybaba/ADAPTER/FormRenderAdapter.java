package com.surveybaba.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.surveybaba.BarCodeActivity;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormDetailsActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.Utility;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 16-Feb-17.
 */

public class FormRenderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // List
    private final List<FormDetailData> viewTypes;
    // Activity
    private final FormDetailsActivity activity;
    // Interface
    Utility.onPhotoCaptured onPhotoCaptured;

    public static String barcodeResult = "";

    // View Type
    private final int VIEW_TYPE_RADIO_GROUP      =  1;
    private final int VIEW_TYPE_SELECT           =  2;
    private final int VIEW_TYPE_TEXT             =  3;
    private final int VIEW_TYPE_DATE             =  4;
    private final int VIEW_TYPE_RATINGS          =  5;
    private final int VIEW_TYPE_SCORE            =  6;
    private final int VIEW_TYPE_CAMERA           =  7;
    private final int VIEW_TYPE_VIDEO            =  8;
    private final int VIEW_TYPE_TEXT_AREA        =  9;
    private final int VIEW_TYPE_CHECKBOX_GROUP   = 10;
    private final int VIEW_TYPE_HEADER           = 11;
    private final int VIEW_TYPE_NUMBER           = 12;
    private final int VIEW_TYPE_FILE             = 13;
    private final int VIEW_TYPE_AUDIO            = 14;
    private final int VIEW_TYPE_PARAGRAPH        = 15;
    private final int VIEW_TYPE_THUMSUP_UPLOADER = 16;
    private final int VIEW_TYPE_SMILEY_UPLOADER  = 17;
    private final int VIEW_TYPE_BAR_CODE         = 18;
    //private final int VIEW_TYPE_FORM_BG_COLOR    = 18;
    //private final int VIEW_TYPE_FORM_LOGO        = 19;
    //private final int VIEW_TYPE_FORM_SNO         = 20;

    public static final String VIEW_TYPE_RADIO_GROUP_STR      = "radio-group";
    public static final String VIEW_TYPE_SELECT_STR           = "select";
    public static final String VIEW_TYPE_TEXT_STR             = "text";
    public static final String VIEW_TYPE_DATE_STR             = "date";
    public static final String VIEW_TYPE_RATINGS_STR          = "starRating";
    public static final String VIEW_TYPE_SCORE_STR            = "scoreRating";
    public static final String VIEW_TYPE_CAMERA_STR           = "cameraUploader";
    public static final String VIEW_TYPE_VIDEO_STR            = "videoUploader";
    public static final String VIEW_TYPE_TEXT_AREA_STR        = "textarea";
    public static final String VIEW_TYPE_NUMBER_STR           = "number";
    public static final String VIEW_TYPE_HEADER_STR           = "header";
    public static final String VIEW_TYPE_CHECKBOX_GROUP_STR   = "checkbox-group";
    public static final String VIEW_TYPE_FILE_STR             = "file";
    public static final String VIEW_TYPE_AUDIO_STR            = "audioUploader";
    public static final String VIEW_TYPE_PARAGRAPH_STR        = "paragraph";
    public static final String VIEW_TYPE_THUMSUP_UPLOADER_STR = "thumsupUploader";
    public static final String VIEW_TYPE_SMILEY_UPLOADER_STR  = "smileyUploader";
    public static final String VIEW_TYPE_BAR_CODE_STR         = "barcodeReader";
    //public static final String VIEW_TYPE_FORM_BG_COLOR_STR    = "formbg_color";
    //public static final String VIEW_TYPE_FORM_LOGO_STR        = "form_logo";
    //public static final String VIEW_TYPE_FORM_SNO_STR         = "form_sno";

    private static boolean isSNo = false;
    private static final int ColorGreen = Color.parseColor("#7ED321");
    private static final int ColorWhite = Color.parseColor("#FFFFFFFF");

//------------------------------------------------------- Constructor ----------------------------------------------------------------------------------------------------------------------

    public FormRenderAdapter(Activity activity, List<FormDetailData> viewTypes, Utility.onPhotoCaptured onPhotoCaptured) {
        this.activity = (FormDetailsActivity) activity;
        this.viewTypes = viewTypes;
        this.onPhotoCaptured = onPhotoCaptured;
        isSNo = Utility.getBooleanSavedData(activity,Utility.FORM_SNO);
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
                return new RadioGroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_radio_group, parent, false));
            case VIEW_TYPE_CHECKBOX_GROUP:
                return new CheckboxGroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_checkbox_group, parent, false));
            case VIEW_TYPE_SELECT:
                return new SelectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_text_selection_view, parent, false));
            case VIEW_TYPE_TEXT:
                return new EditTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_view, parent, false));
            case VIEW_TYPE_TEXT_AREA:
                return new EditTextAreaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_area_view, parent, false));
            case VIEW_TYPE_NUMBER:
                return new EditTextNumberHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_edittext_number_type,parent, false));
            case VIEW_TYPE_HEADER:
                return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_header_view, parent, false));
            case VIEW_TYPE_DATE:
                return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_datetime_view, parent, false));
            case VIEW_TYPE_RATINGS:
                return new RatingsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_ratings, parent, false));
            case VIEW_TYPE_SCORE:
                return new ScoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_score_bar, parent, false));
            case VIEW_TYPE_CAMERA:
                return new CameraHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_camera_view, parent, false));
            case VIEW_TYPE_VIDEO:
                return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_video_view, parent, false));
            case VIEW_TYPE_FILE:
                return new FileHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_file_view, parent, false));
            case VIEW_TYPE_AUDIO:
                return new AudioHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_audio_view, parent, false));
            case VIEW_TYPE_PARAGRAPH:
                return new ParagraphHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_paragraph_view, parent, false));
            case VIEW_TYPE_THUMSUP_UPLOADER:
                return new THUMPSUP_UPLOADER_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_thumsup_view, parent, false));
            case VIEW_TYPE_SMILEY_UPLOADER:
                return new SMILEY_UPLOADER_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_smiley_view, parent, false));
            case VIEW_TYPE_BAR_CODE:
                return new BarCode_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_barcode_view, parent, false));
            default:
                return new TempHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false));
        }
    }

//------------------------------------------------------- on Bind View Holder ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FormDetailData bin = viewTypes.get(position);
        switch (getItemViewType(position)) {

//------------------------------------------------------- Radio Button ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_RADIO_GROUP:
                if (holder instanceof RadioGroupHolder) {
                    RadioGroupHolder radioGroupHolder = ((RadioGroupHolder) holder);
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
                        @SuppressLint("InflateParams")
                        View viewRadioButton = LayoutInflater.from(activity).inflate(R.layout.custom_radio_btn, null);
                        viewRadioButton.setId(id);
                        id++;
                        radioGroupHolder.llRadioGroupLayout.addView(viewRadioButton);
                        ((RadioButton) viewRadioButton).setText(binFormDetailOps.getLabel());
                        ((RadioButton) viewRadioButton).setChecked(binFormDetailOps.isSelected());
                        viewRadioButton.setTag(radioGroupHolder);
                        ((RadioButton) viewRadioButton).setOnClickListener(view -> {
                            RadioGroupHolder radioGroupHolder2 = (RadioGroupHolder) view.getTag();
                            LinearLayout linearLayout = radioGroupHolder2.llRadioGroupLayout;
                            int posOptionSelected = -1;
                            for (int k = 0; k < linearLayout.getChildCount(); k++) {
                                RadioButton rb = ((RadioButton) linearLayout.getChildAt(k));
                                rb.setChecked(false);
                                if (view.getId() == rb.getId()) {
                                    posOptionSelected = k;
                                }
                                bin.getSelvalues().get(k).setSelected(false);
                            }
                            ((RadioButton) view).setChecked(true);
                            bin.getSelvalues().get(posOptionSelected).setSelected(true);
                        });
                    }
                }
                break;

//------------------------------------------------------- CheckBox ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_CHECKBOX_GROUP:
                if (holder instanceof CheckboxGroupHolder) {
                    CheckboxGroupHolder checkboxGroupHolder = ((CheckboxGroupHolder) holder);
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
                            @SuppressLint("InflateParams")
                            View customCheckBoxLayout = LayoutInflater.from(activity).inflate(R.layout.custom_chkbox_btn, null);
                            customCheckBoxLayout.setId(id);
                            id++;
                            checkboxGroupHolder.llCheckBoxGroupLayout.addView(customCheckBoxLayout);
                            CheckBox checkBox = customCheckBoxLayout.findViewById(R.id.checkBox);
                            checkBox.setText(binFormDetailOps.getLabel());
                            checkBox.setChecked(binFormDetailOps.isSelected());
                            checkBox.setTag(i);
                            checkBox.setOnClickListener(view -> {
                                int pos = (int) view.getTag();
                                bin.getSelvalues().get(pos).setSelected(!bin.getSelvalues().get(pos).isSelected());
                            });
                        }
                    }

                }
                break;

//-------------------------------------------------------  Select ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SELECT:
                if (holder instanceof SelectHolder) {
                    SelectHolder selectHolder = ((SelectHolder) holder);
                    String selectedText = "";
                    for (FormDetailData.FormOptions binOp : bin.getSelvalues()) {
                        if (binOp.isSelected()) {
                            selectedText = binOp.getLabel();
                            break;
                        }
                    }
                    selectHolder.txtQuestion.setText(bin.getLabel());


                    if(isSNo){
                        selectHolder.txtSNoSelect.setVisibility(View.VISIBLE);
                        selectHolder.txtSNoSelect.setText((position+1)+".");
                    }
                    else{
                        selectHolder.txtSNoSelect.setVisibility(View.GONE);
                        selectHolder.txtSNoSelect.setText("");
                    }

                    selectHolder.tvName.setText(Utility.isEmptyString(selectedText) ? activity.getString(R.string.select) : selectedText);
                    selectHolder.rlSelect.setTag(position);
                    selectHolder.rlSelect.setOnClickListener(view -> {
                        ArrayList<String> stringArrayList = new ArrayList<>();
                        for (FormDetailData.FormOptions binOp : bin.getSelvalues()) {
                            stringArrayList.add(binOp.getLabel());
                        }
                        String[] selectionArray = stringArrayList.toArray(new String[stringArrayList.size()]);
                        Utility.openSelectDialog(activity, selectionArray, position16 -> {
                            selectHolder.tvName.setText(selectionArray[position16]);
                            for (int i = 0; i < bin.getSelvalues().size(); i++) {
                                bin.getSelvalues().get(i).setSelected(false);
                            }
                            bin.getSelvalues().get(position16).setSelected(true);
                        });
                    });
                }
                break;

//------------------------------------------------------- Text  ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_TEXT:
                if (holder instanceof EditTextHolder) {
                    EditTextHolder editTextHolder = ((EditTextHolder) holder);
                    editTextHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        editTextHolder.txtSNoEditTextView.setVisibility(View.VISIBLE);
                        editTextHolder.txtSNoEditTextView.setText((position+1)+".");
                    }
                    else{
                        editTextHolder.txtSNoEditTextView.setVisibility(View.GONE);
                        editTextHolder.txtSNoEditTextView.setText("");
                    }

                    editTextHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    editTextHolder.edtDescription.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            bin.setFill_value(charSequence.toString());
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                    editTextHolder.edtDescription.setOnEditorActionListener((textView, actionId, event) -> true);
                }
                break;

//------------------------------------------------------- Text Area ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_TEXT_AREA:
                if (holder instanceof EditTextAreaHolder) {
                    EditTextAreaHolder editTextHolder = ((EditTextAreaHolder) holder);
                    editTextHolder.txtQuestion.setText(bin.getLabel());


                    if(isSNo){
                        editTextHolder.txtSNoEditTextArea.setVisibility(View.VISIBLE);
                        editTextHolder.txtSNoEditTextArea.setText((position+1)+".");
                    }
                    else{
                        editTextHolder.txtSNoEditTextArea.setVisibility(View.GONE);
                        editTextHolder.txtSNoEditTextArea.setText("");
                    }


                    editTextHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    editTextHolder.edtDescription.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            bin.setFill_value(charSequence.toString());
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                }
                break;

//------------------------------------------------------- Number ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_NUMBER:
                if (holder instanceof EditTextNumberHolder) {
                    EditTextNumberHolder editTextHolder = ((EditTextNumberHolder) holder);
                    editTextHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        editTextHolder.txtSNoEditTextNumber.setVisibility(View.VISIBLE);
                        editTextHolder.txtSNoEditTextNumber.setText((position+1)+".");
                    }
                    else{
                        editTextHolder.txtSNoEditTextNumber.setVisibility(View.GONE);
                        editTextHolder.txtSNoEditTextNumber.setText("");
                    }

                    editTextHolder.edtDescription.setText(Utility.isEmptyString(bin.getFill_value()) ? "" : bin.getFill_value());
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    editTextHolder.edtDescription.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            bin.setFill_value(charSequence.toString());
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                    editTextHolder.edtDescription.setOnEditorActionListener((textView, actionId, event) -> true);
                }
                break;

//------------------------------------------------------- Header ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_HEADER:
                if (holder instanceof HeaderHolder) {
                    HeaderHolder editTextHolder = ((HeaderHolder) holder);
                    editTextHolder.txtQuestion.setText(bin.getLabel());
                    bin.setFill_value(bin.getLabel());
                }
                break;

//------------------------------------------------------- Date ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_DATE:
                if (holder instanceof DateHolder) {
                    DateHolder dateHolder = ((DateHolder) holder);
                    dateHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        dateHolder.txtSNoDateTime.setVisibility(View.VISIBLE);
                        dateHolder.txtSNoDateTime.setText((position+1)+".");
                    }
                    else{
                        dateHolder.txtSNoDateTime.setVisibility(View.GONE);
                        dateHolder.txtSNoDateTime.setText("");
                    }

                    dateHolder.tvdatetime.setText(Utility.isEmptyString(bin.getFill_value()) ? activity.getString(R.string.select_date_time) : bin.getFill_value());
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    dateHolder.rlSelect.setOnClickListener(view -> Utility.openDatePickerDialog(activity, date -> {
                          dateHolder.tvdatetime.setText(date);
                        Utility.openTimePickerDialog(activity, time -> {
                            dateHolder.tvdatetime.setText(date + " " + time);
                            bin.setFill_value(date + " " + time);

                        });
                    }));
                }
                break;

//------------------------------------------------------- Rating ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_RATINGS:
                if (holder instanceof RatingsHolder) {
                    RatingsHolder ratingsHolder = ((RatingsHolder) holder);
                    ratingsHolder.txtQuestion.setText(bin.getLabel());


                    if(isSNo){
                        ratingsHolder.txtSNoRating.setVisibility(View.VISIBLE);
                        ratingsHolder.txtSNoRating.setText((position+1)+".");
                    }
                    else{
                        ratingsHolder.txtSNoRating.setVisibility(View.GONE);
                        ratingsHolder.txtSNoRating.setText("");
                    }

                    ratingsHolder.ratingBar.setRating(Utility.isEmptyString(bin.getFill_value()) ? 0 : Float.parseFloat(bin.getFill_value()));

                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    ratingsHolder.ratingBar.setOnRatingBarChangeListener((ratingBar, ratings, b) -> {
                        bin.setFill_value("" + ratings);
                    });
                }
                break;

//------------------------------------------------------- Score ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SCORE:
                if (holder instanceof ScoreHolder) {
                    ScoreHolder scoreHolder = (ScoreHolder) holder;
                    scoreHolder.txtQuestion.setText(bin.getLabel());
                    scoreHolder.llScoreViewParent.setTag(position);

                    if (isSNo) {
                        scoreHolder.txtSNoScoreBar.setVisibility(View.VISIBLE);
                        scoreHolder.txtSNoScoreBar.setText((position + 1) + ".");
                    } else {
                        scoreHolder.txtSNoScoreBar.setVisibility(View.GONE);
                        scoreHolder.txtSNoScoreBar.setText("");
                    }


                    if (Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("0");
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
                    for (int i = 1; i <= listTxtViews.size(); i++) {
                        listTxtViews.get(i - 1).setSelected(i <= Integer.parseInt(bin.getFill_value()));
                        listTxtViews.get(i - 1).setTag(i);
                        listTxtViews.get(i - 1).setOnClickListener(view -> {
                            bin.setFill_value("" + (Integer) view.getTag());
                            Log.e("SCORE", "onScoreClick: " + bin.getFill_value());
                            for (int i12 = 1; i12 <= listTxtViews.size(); i12++) {
                                listTxtViews.get(i12 - 1).setSelected(i12 <= Integer.parseInt(bin.getFill_value()));
                            }
                            notifyDataSetChanged();
                        });
                    }

                }
                break;

//------------------------------------------------------- Camera/Image ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_CAMERA: // VIEW_TYPE_IMAGE
                if (holder instanceof CameraHolder) {
                    CameraHolder cameraHolder = ((CameraHolder) holder);
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
                        cameraHolder.imgCaptured.setImageResource(R.drawable.icon_camera);
                    }
                    else{
                        // Check if local path is available else display download image
                        Bitmap bitmap =  (ImageFileUtils.getBitmapFromFilePath((bin.getFill_value()).split("#")[1]));
                        Glide.with(activity).load(bitmap).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(cameraHolder.imgCaptured);
                    }
                    cameraHolder.imgCaptured.setTag(position);

                    cameraHolder.imgCaptured.setOnClickListener(view -> activity.getUtility().openImageSelection(activity, activity.getSystemPermission(), activity.getImageFileUtils(), (int) view.getTag(), (path, position1) -> onPhotoCaptured.getPath(path, position1)));
                }
                break;

//------------------------------------------------------- Video ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_VIDEO:
                if (holder instanceof VideoHolder) {
                    VideoHolder videoHolder = (VideoHolder) holder;
                    videoHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        videoHolder.txtSNoVideo.setVisibility(View.VISIBLE);
                        videoHolder.txtSNoVideo.setText((position+1)+".");
                    }
                    else{
                        videoHolder.txtSNoVideo.setVisibility(View.GONE);
                        videoHolder.txtSNoVideo.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        bin.setFill_value("");
                        //videoHolder.imgVideo.setImageResource(R.drawable.icon_video);
                    } else {
                        String[] str = bin.getFill_value().split(",");
                        if(str.length > 1){
                            videoHolder.tv_videoUploadName.setText("Multiple File Selected");
                        }
                        else{
                            videoHolder.tv_videoUploadName.setText("Video Selected");
                            //videoHolder.tv_videoUploadName.setText(bin.getFill_value());
                        }
                        //videoHolder.imgVideo.setImageBitmap(activity.getImageFileUtils().getThumbnailImage(bin.getFill_value()));
                    }
                    videoHolder.imgVideo.setTag(position);

                    videoHolder.imgVideo.setOnClickListener(view -> activity.getUtility().openVideoFilePicker(activity, activity.getSystemPermission(), (int) view.getTag(), (path, position12) -> onPhotoCaptured.getPath(path, position12)));

//                    videoHolder.llImageVideo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int position = (int) view.getTag();
//                            FormDetailData binViewType = viewTypes.get(position);
//                            String path = binViewType.getFill_value();
//                            if (Utility.isEmptyString(path)) {
//                                if (activity.getSystemPermission().isExternalStorage()) {
//                                    if (activity.getSystemPermission().isCamera()) {
//                                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                        if (takeVideoIntent.resolveActivity(activity.getPackageManager()) != null) {
//                                            File destFileTemp = activity.getImageFileUtils().getDestinationFileVideo(activity.getImageFileUtils().getRootDirFileVideo(activity), binViewType.getInput_id());
//                                            Uri videoURI = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", destFileTemp);
//                                            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
//                                            onPhotoCaptured.getPath(destFileTemp.getAbsolutePath(), position);
//                                            activity.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                                        }
//                                    }
//                                }
//                            } else {
//                                Intent intent = new Intent(activity, VideoViewActivity.class);
//                                intent.putExtra(Utility.key_intent_videoPath, path);
//                                activity.startActivity(intent);
//                            }
//                        }
//                    });
                }
                break;

//------------------------------------------------------- File ----------------------------------------------------------------------------------------------------------------------
            case VIEW_TYPE_FILE:
                if (holder instanceof FileHolder) {
                    FileHolder fileHolder = ((FileHolder) holder);
                    fileHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        fileHolder.txtSNoFile.setVisibility(View.VISIBLE);
                        fileHolder.txtSNoFile.setText((position+1)+".");
                    }
                    else{
                        fileHolder.txtSNoFile.setVisibility(View.GONE);
                        fileHolder.txtSNoFile.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        bin.setFill_value("");
                    } else {
                        String[] str = bin.getFill_value().split(",");
                        if(str.length > 1){
                            fileHolder.tv_fileUploadName.setText("Multiple File Selected");
                        }
                        else{
                            fileHolder.tv_fileUploadName.setText("File Selected");
                        }
                    }
                    fileHolder.rlFileCapture.setTag(position);
                    // Multiple File Upload is True
                    if(bin.getMultiple().equals("t")){
                        fileHolder.rlFileCapture.setOnClickListener(view -> activity.getUtility().openFilePickerMultiple(activity, activity.getSystemPermission(), (int) view.getTag(), (path, position13) -> onPhotoCaptured.getPath(path, position13)));
                    }
                    // Single File Upload is True
                    else{
                        fileHolder.rlFileCapture.setOnClickListener(view -> activity.getUtility().openFilePicker(activity, activity.getSystemPermission(), (int) view.getTag(), (path, position14) -> onPhotoCaptured.getPath(path, position14)));
                    }

                }
                break;

//------------------------------------------------------- Audio ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_AUDIO:
                if (holder instanceof AudioHolder) {
                    AudioHolder audioHolder = ((AudioHolder) holder);
                    audioHolder.txtQuestion.setText(bin.getLabel());

                    if(isSNo){
                        audioHolder.txtSNoAudio.setVisibility(View.VISIBLE);
                        audioHolder.txtSNoAudio.setText((position+1)+".");
                    }
                    else{
                        audioHolder.txtSNoAudio.setVisibility(View.GONE);
                        audioHolder.txtSNoAudio.setText("");
                    }

                    if (Utility.isEmptyString(bin.getFill_value())) {
                        bin.setFill_value("");
                    } else {
                        String[] str = bin.getFill_value().split(",");
                        if(str.length > 1){
                            audioHolder.tv_audioUploadName.setText("Multiple File Selected");
                        }
                        else{
                            audioHolder.tv_audioUploadName.setText("Audio Selected");
                        }
                    }
                    audioHolder.rlFileCaptureAudio.setTag(position);
                    audioHolder.rlFileCaptureAudio.setOnClickListener(view -> activity.getUtility().openAudioFilePicker(activity, activity.getSystemPermission(), (int) view.getTag(), (path, position15) -> onPhotoCaptured.getPath(path, position15)));
                }
                break;

//------------------------------------------------------- ParaGraph ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_PARAGRAPH:
                if (holder instanceof ParagraphHolder) {
                    ParagraphHolder paragraphHolder = ((ParagraphHolder) holder);
                    paragraphHolder.txt_paragraph.setText(bin.getLabel());
                    bin.setFill_value(bin.getLabel());
                }
                break;

//------------------------------------------------------- Thums Up ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_THUMSUP_UPLOADER:
                if (holder instanceof THUMPSUP_UPLOADER_Holder) {
                    THUMPSUP_UPLOADER_Holder thumpsup_uploader_holder = ((THUMPSUP_UPLOADER_Holder) holder);
                    thumpsup_uploader_holder.thumsup_textview.setText(bin.getLabel());

                    if(isSNo){
                        thumpsup_uploader_holder.txtSNoThumsup.setVisibility(View.VISIBLE);
                        thumpsup_uploader_holder.txtSNoThumsup.setText((position+1)+".");
                    }
                    else{
                        thumpsup_uploader_holder.txtSNoThumsup.setVisibility(View.GONE);
                        thumpsup_uploader_holder.txtSNoThumsup.setText("");
                    }

//                    if(fromData.isRequired()){
//                        thums_up_holder.form_thumsup_error_imageview.setVisibility(View.VISIBLE);
//                    }
//                    else{
//                        thums_up_holder.form_thumsup_error_imageview.setVisibility(View.GONE);
//                    }
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }
                    // When Thum Up Press
                    thumpsup_uploader_holder.thums_up_imageview.setOnClickListener(view -> {
                        bin.setFill_value("up");
                        thumpsup_uploader_holder.thums_up_imageview.setImageResource(R.drawable.thumbs_up_fill);
                        thumpsup_uploader_holder.thums_down_imageview.setImageResource(R.drawable.thumbs_down);
                    });
                    // When Thum down Press
                    thumpsup_uploader_holder.thums_down_imageview.setOnClickListener(view -> {
                        bin.setFill_value("down");
                        thumpsup_uploader_holder.thums_down_imageview.setImageResource(R.drawable.thumbs_down_fill);
                        thumpsup_uploader_holder.thums_up_imageview.setImageResource(R.drawable.thumbs_up);
                    });

                }
                break;

//------------------------------------------------------- Smiley ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_SMILEY_UPLOADER:
                if (holder instanceof SMILEY_UPLOADER_Holder) {
                    SMILEY_UPLOADER_Holder smiley_holder = ((SMILEY_UPLOADER_Holder) holder);
                    smiley_holder.smiley_textview.setText(bin.getLabel());

                    if(isSNo){
                        smiley_holder.txtSNoSmiley.setVisibility(View.VISIBLE);
                        smiley_holder.txtSNoSmiley.setText((position+1)+".");
                    }
                    else{
                        smiley_holder.txtSNoSmiley.setVisibility(View.GONE);
                        smiley_holder.txtSNoSmiley.setText("");
                    }

//                    if(bin.isRequired()){
//                        smiley_holder.smiley_error_imageview.setVisibility(View.VISIBLE);
//                    }
//                    else{
//                        smiley_holder.smiley_error_imageview.setVisibility(View.GONE);
//                    }
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                    }

                    // Here When User Press any emoji!
                    // 1 emoji press
                    smiley_holder.smiley_emoji_1.setOnClickListener(view -> {
                        // When Press
                        bin.setFill_value("1");
//                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.green));
//                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley1.setBackgroundColor(ColorGreen);
                        smiley_holder.ll_Smiley2.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley3.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley4.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley5.setBackgroundColor(ColorWhite);
                    });
                    // 2 emoji press
                    smiley_holder.smiley_emoji_2.setOnClickListener(view -> {
                        // When Press
                        bin.setFill_value("2");
//                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.green));
//                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley1.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley2.setBackgroundColor(ColorGreen);
                        smiley_holder.ll_Smiley3.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley4.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley5.setBackgroundColor(ColorWhite);
                    });
                    // 3 emoji press
                    smiley_holder.smiley_emoji_3.setOnClickListener(view -> {
                        // When Press
                        bin.setFill_value("3");
//                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.green));
//                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley1.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley2.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley3.setBackgroundColor(ColorGreen);
                        smiley_holder.ll_Smiley4.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley5.setBackgroundColor(ColorWhite);
                    });
                    // 4 emoji press
                    smiley_holder.smiley_emoji_4.setOnClickListener(view -> {
                        // When Press
                        bin.setFill_value("4");
//                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.green));
//                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.white));
                        smiley_holder.ll_Smiley1.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley2.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley3.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley4.setBackgroundColor(ColorGreen);
                        smiley_holder.ll_Smiley5.setBackgroundColor(ColorWhite);

                    });
                    // 5 emoji press
                    smiley_holder.smiley_emoji_5.setOnClickListener(view -> {
                        // When Press
                        bin.setFill_value("5");
//                        smiley_holder.ll_Smiley1.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley2.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley3.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley4.setBackgroundColor(activity.getResources().getColor(R.color.white));
//                        smiley_holder.ll_Smiley5.setBackgroundColor(activity.getResources().getColor(R.color.green));
                        smiley_holder.ll_Smiley1.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley2.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley3.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley4.setBackgroundColor(ColorWhite);
                        smiley_holder.ll_Smiley5.setBackgroundColor(ColorGreen);
                    });
                }
                break;



//------------------------------------------------------- Bar Code ----------------------------------------------------------------------------------------------------------------------

            case VIEW_TYPE_BAR_CODE:
                if (holder instanceof BarCode_Holder) {
                    BarCode_Holder barCode_holder= ((BarCode_Holder) holder);
                    barCode_holder.barcode_textview.setText(bin.getLabel());

                    if(isSNo){
                        barCode_holder.txtSNoBarCode.setVisibility(View.VISIBLE);
                        barCode_holder.txtSNoBarCode.setText((position+1)+".");
                    }
                    else{
                        barCode_holder.txtSNoBarCode.setVisibility(View.GONE);
                        barCode_holder.txtSNoBarCode.setText("");
                    }
                    Log.e("Form", ""+bin.getFill_value());
                    if(Utility.isEmptyString(bin.getFill_value())){
                        bin.setFill_value("");
                        barCode_holder.barcodeResult.setHint("Result not found");
                    }
                    else{
                        barCode_holder.barcodeResult.setText(bin.getFill_value());
                    }
                    barCode_holder.barcodeCaptured.setTag(position);
                    barCode_holder.barcodeCaptured.setOnClickListener(view -> {

                        activity.getUtility().openBarCode(activity, (int) view.getTag(),(path, position17) -> {
                            onPhotoCaptured.getPath(path,position17);
                        });
                    });

                }
                break;


//------------------------------------------------------- Default ----------------------------------------------------------------------------------------------------------------------

            default:
                if (holder instanceof TempHolder) {
                    TempHolder tempHolder = ((TempHolder) holder);
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
        ImageView error_imageview;

        SelectHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            tvName = itemView.findViewById(R.id.tvName);
            rlSelect = itemView.findViewById(R.id.rlSelect);
            error_imageview = itemView.findViewById(R.id.select_error_imageview);
            txtSNoSelect = itemView.findViewById(R.id.txtSNoSelect);
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





}