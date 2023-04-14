package com.surveybaba.backup;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.R;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackupManagerActivity extends AppCompatActivity {
    String TAG = BackupManagerActivity.class.getSimpleName();
    ImageFileUtils imageFileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_backup_manager);
        imageFileUtils = new ImageFileUtils();
        initDatabase();
        init();
    }

    private DataBaseHelper dataBaseHelper;

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(BackupManagerActivity.this);
    }

    private LinearLayout llBackupAll, llBackupProjectWise, llBackupOfDay;

    private void init() {
        llBackupAll = findViewById(R.id.llBackupAll);
        llBackupProjectWise = findViewById(R.id.llBackupProjectWise);
        llBackupOfDay = findViewById(R.id.llBackupOfDay);

        llBackupAll.setOnClickListener(myClick);
        llBackupOfDay.setOnClickListener(myClick);
        llBackupProjectWise.setOnClickListener(myClick);
        /*String csv = "";
        String jsonArrayString = "[{\n" +
                "\t\t\"formid\": \"6\",\n" +
                "\t\t\"record_date\": \"2021-08-02 13:01:19\",\n" +
                "\t\t\"uniq_id\": \"5\",\n" +
                "\t\t\"data\": \"{\\\"select-1627639476968-0\\\":\\\"Option 1\\\",\\\"audioUploader-1627639483176-0\\\":\\\"\\\",\\\"checkbox-group-1627639472617-0\\\":\\\"Option 1\\\",\\\"videoUploader-1627639482424-0\\\":\\\"\\\",\\\"geom_array\\\":\\\"[{\\\\\\\"latitude\\\\\\\":23.073432330443854,\\\\\\\"longitude\\\\\\\":72.48270951211452},{\\\\\\\"latitude\\\\\\\":23.110949121248595,\\\\\\\"longitude\\\\\\\":72.69666697829962},{\\\\\\\"latitude\\\\\\\":22.885691626283236,\\\\\\\"longitude\\\\\\\":72.68630895763636},{\\\\\\\"latitude\\\\\\\":22.91431659740817,\\\\\\\"longitude\\\\\\\":72.49339107424021}]\\\",\\\"radio-group-1627639476592-0\\\":\\\"Option 2\\\",\\\"starRating-1627639480856-0\\\":\\\"4.5\\\",\\\"header-5\\\":\\\"\\\",\\\"scoreRating-1627639481696-0\\\":\\\"9\\\",\\\"number-1627639475744-0\\\":\\\"330\\\",\\\"hidden-1627639475289-0\\\":\\\"\\\",\\\"file-1627639473905-0\\\":\\\"http://surveybaba.com/web_api/uploads/pdf_02_08_2021_130036654.pdf\\\",\\\"geom_type\\\":\\\"Line\\\",\\\"paragraph-8\\\":\\\"\\\",\\\"text-1627639479184-0\\\":\\\"tf1\\\",\\\"textarea-1627639480104-0\\\":\\\"tf area\\\",\\\"autocomplete-1627639470563-0\\\":\\\"\\\",\\\"date-1627639473216-0\\\":\\\"02-08-2021 13:00:32\\\"}\",\n" +
                "\t\t\"user_id\": \"19\",\n" +
                "\t\t\"project_id\": \"3\",\n" +
                "\t\t\"latitude\": \"22.9818233\",\n" +
                "\t\t\"version\": \"1.0.13\",\n" +
                "\t\t\"longitude\": \"72.6008983\"\n" +
                "\t}, {\n" +
                "\t\t\"formid\": \"6\",\n" +
                "\t\t\"record_date\": \"2021-08-02 13:01:19\",\n" +
                "\t\t\"uniq_id\": \"5\",\n" +
                "\t\t\"data\": \"{\\\"select-1627639476968-0\\\":\\\"Option 1\\\",\\\"audioUploader-1627639483176-0\\\":\\\"\\\",\\\"checkbox-group-1627639472617-0\\\":\\\"Option 1\\\",\\\"videoUploader-1627639482424-0\\\":\\\"\\\",\\\"geom_array\\\":\\\"[{\\\\\\\"latitude\\\\\\\":23.073432330443854,\\\\\\\"longitude\\\\\\\":72.48270951211452},{\\\\\\\"latitude\\\\\\\":23.110949121248595,\\\\\\\"longitude\\\\\\\":72.69666697829962},{\\\\\\\"latitude\\\\\\\":22.885691626283236,\\\\\\\"longitude\\\\\\\":72.68630895763636},{\\\\\\\"latitude\\\\\\\":22.91431659740817,\\\\\\\"longitude\\\\\\\":72.49339107424021}]\\\",\\\"radio-group-1627639476592-0\\\":\\\"Option 2\\\",\\\"starRating-1627639480856-0\\\":\\\"4.5\\\",\\\"header-5\\\":\\\"\\\",\\\"scoreRating-1627639481696-0\\\":\\\"9\\\",\\\"number-1627639475744-0\\\":\\\"330\\\",\\\"hidden-1627639475289-0\\\":\\\"\\\",\\\"file-1627639473905-0\\\":\\\"http://surveybaba.com/web_api/uploads/pdf_02_08_2021_130036654.pdf\\\",\\\"geom_type\\\":\\\"Line\\\",\\\"paragraph-8\\\":\\\"\\\",\\\"text-1627639479184-0\\\":\\\"tf1\\\",\\\"textarea-1627639480104-0\\\":\\\"tf area\\\",\\\"autocomplete-1627639470563-0\\\":\\\"\\\",\\\"date-1627639473216-0\\\":\\\"02-08-2021 13:00:32\\\"}\",\n" +
                "\t\t\"user_id\": \"19\",\n" +
                "\t\t\"project_id\": \"3\",\n" +
                "\t\t\"latitude\": \"22.9818233\",\n" +
                "\t\t\"version\": \"1.0.13\",\n" +
                "\t\t\"longitude\": \"72.6008983\"\n" +
                "\t}\n" +
                "\n" +
                "]";
        try {
            JSONArray docs = new JSONArray(jsonArrayString);
            File file = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(this), "csv");
            csv = CDL.toString(docs);
            Log.e(TAG,"Data has been Successfully Written to "+ file);
            Log.e(TAG,csv);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = CDL.toJSONArray(csv);
            Log.e(TAG,jsonArray.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    private View.OnClickListener myClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.llBackupAll:
                    exportCSV(getResources().getString(R.string.app_name) + "_" + "BackUpAll.csv", dataBaseHelper.getExportData());
                    break;
                case R.id.llBackupOfDay:
                    exportCSV(getResources().getString(R.string.app_name) + "_" + "DailyBackup" + "_" + getDateDDMMYYYY() + ".csv", dataBaseHelper.getExportData());
                    break;
                case R.id.llBackupProjectWise:
                    processToBackupProjectWise();
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void exportCSV(String filename, String query) {
        try {
            dataBaseHelper.open();
            Cursor c = dataBaseHelper.executeCursor(query);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));
            if (!sdCardDir.exists()) {
                if (!sdCardDir.mkdirs()) {
                    return;
                }
            }
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + ",");
                    } else {
                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }

                    bw.newLine();
                }
                bw.flush();
                dataBaseHelper.close();
                Utility.showInfoDialog(BackupManagerActivity.this, "Exported Successfully.");
            }
        } catch (Exception ex) {
            dataBaseHelper.close();
            Utility.showInfoDialog(BackupManagerActivity.this, ex.getMessage());
        }
    }

    private void processToBackupProjectWise() {
        try {
            dataBaseHelper.open();
            Cursor curProject = dataBaseHelper.executeCursor(dataBaseHelper.getProjectWiseExportData());
            if (curProject != null && curProject.getCount() > 0) {
                curProject.moveToFirst();
                for (int cnt = 0; cnt < curProject.getCount(); cnt++) {
                    String pName = curProject.getString(curProject.getColumnIndex("project"));
                    String pID = curProject.getString(curProject.getColumnIndex("id"));
                    Cursor c = dataBaseHelper.executeCursor(dataBaseHelper.getExportData());
                    int rowcount = 0;
                    int colcount = 0;
                    File sdCardDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));
                    if (!sdCardDir.exists()) {
                        if (!sdCardDir.mkdirs()) {
                            return;
                        }
                    }
                    String filename = pName + "_" + getDateDDMMYYYY() + ".csv";
                    File saveFile = new File(sdCardDir, filename);
                    FileWriter fw = new FileWriter(saveFile);
                    BufferedWriter bw = new BufferedWriter(fw);
                    rowcount = c.getCount();
                    colcount = c.getColumnCount();
                    if (rowcount > 0) {
                        c.moveToFirst();
                        for (int i = 0; i < colcount; i++) {
                            if (i != colcount - 1) {
                                bw.write(c.getColumnName(i) + ",");
                            } else {
                                bw.write(c.getColumnName(i));
                            }
                        }
                        bw.newLine();
                        for (int i = 0; i < rowcount; i++) {
                            c.moveToPosition(i);
                            for (int j = 0; j < colcount; j++) {
                                if (j != colcount - 1)
                                    bw.write(c.getString(j) + ",");
                                else
                                    bw.write(c.getString(j));
                            }

                            bw.newLine();
                        }
                        bw.flush();
                        dataBaseHelper.close();
                    }
                    curProject.moveToNext();
                }
            }
            Utility.showInfoDialog(BackupManagerActivity.this, "Exported Successfully.");
        } catch (Exception ex) {
            dataBaseHelper.close();
            Utility.showInfoDialog(BackupManagerActivity.this, ex.getMessage().toString());
        }
    }

    private String getDateDDMMYYYY() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

}
