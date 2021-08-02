package com.example.colorpalettelib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;

import com.plumamazing.iwatermarkpluslib.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;

public class ColorSelectionActivity extends AppCompatActivity {

    private ImageButton selectColorShow;

    private static final int[] btnIdArray = {R.id.color_btn_0,R.id.color_btn_1,R.id.color_btn_2,R.id.color_btn_3,R.id.color_btn_4,
            R.id.color_btn_5,R.id.color_btn_6,R.id.color_btn_7,R.id.color_btn_8,R.id.color_btn_9,R.id.color_btn_10,
            R.id.color_btn_11,R.id.color_btn_12,R.id.color_btn_13,R.id.color_btn_14,R.id.color_btn_15,R.id.color_btn_16,
            R.id.color_btn_17,R.id.color_btn_18,R.id.color_btn_19,R.id.color_btn_20,R.id.color_btn_21,R.id.color_btn_22,
            R.id.color_btn_23,R.id.color_btn_24};
    private Button[] btn = new Button[btnIdArray.length];
    int[] mainColors;
    final  GradientDrawable [] paletteColorsBtnBgShape = new GradientDrawable[btnIdArray.length];
    private GradientDrawable selectColorShowBgShape;


    private char [] hexCharactersArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    private SeekBar seekbar_red,seekbar_green,seekbar_blue,brightness_seekBar;
    private LinearLayout seekbars_layout;
    private EditText edittext_red, edittext_green, edittext_blue;
    private TextView textview_red,textview_green,textview_blue,progress_textView;
    private Button rgbBtn,hslBtn;
    private LinearLayout layout_HSL_seekBars,layout_RGB_seekBars;

    private SeekBar hsl_seekbar_hue,hsl_seekbar_saturation,hsl_seekbar_lightness;
    private EditText hsl_edittext_hue,hsl_edittext_saturation,hsl_edittext_lightness;

    private ImageView img_eye,edit_color,favorite_color_btn,color_dice,eye_dropper,help;
    private Switch switchHex;
    boolean isHex = false;

    boolean isRGB = false;

    JSONObject jsonNonHEXRGB;
    JSONObject jsonHEXRGB;
    JSONObject jsonNonHEXHSL;
    JSONObject jsonHEXHSL;

    int currentColor = -389632;
    int favoriteColor = -389632;

    private TableRow redRow;
    private LinearLayout greenSeekBarLayout;

    int currentAlpha = 255;
    private int seekR, seekG, seekB;

    private int seekH, seekS, seekL;

    float[] currentColorHSL = new float[3];

    String colorModel = "RGB";

    String filePath = "";

    Class callingClass = null;
    public static Activity activity;

    String colorProperty = "textColor";

    LinearLayout color_palette_layout,favorite_palette_layout;
    TextView textDone;
    boolean isFavoriteEdit = false;
    boolean isFavorite = false;
    private static final int[] favtButtonsIdArray = {R.id.favt_btn_0,R.id.favt_btn_1,R.id.favt_btn_2,R.id.favt_btn_3,R.id.favt_btn_4,
            R.id.favt_btn_5,R.id.favt_btn_6,R.id.favt_btn_7,R.id.favt_btn_8,R.id.favt_btn_9,R.id.favt_btn_10,
            R.id.favt_btn_11,R.id.favt_btn_12,R.id.favt_btn_13,R.id.favt_btn_14,R.id.favt_btn_15,R.id.favt_btn_16,
            R.id.favt_btn_17,R.id.favt_btn_18,R.id.favt_btn_19,R.id.favt_btn_20,R.id.favt_btn_21,R.id.favt_btn_22,
            R.id.favt_btn_23,R.id.favt_btn_24};
    private Button[] favtButtons = new Button[btnIdArray.length];

    private GradientDrawable favtButtonsShapeColor;
    private TableRow colorExtractLayout;
    String imageFilePath = "";
    JSONObject colorExtractor = new JSONObject();
    private static final int[] imageExtractButtonsIdArray = {R.id.select_img_btn_0,R.id.select_img_btn_1,R.id.select_img_btn_2,R.id.select_img_btn_3,R.id.select_img_btn_4,
                                                        R.id.select_img_btn_5};
    private Button[] imageExtractButtons = new Button[imageExtractButtonsIdArray.length];
    final  int [] extractedColors = new int[imageExtractButtonsIdArray.length];
    final  GradientDrawable [] extractedColorsBgShape = new GradientDrawable[imageExtractButtonsIdArray.length];
    private Animation mBounceAnimation;  //By Kashif feature-issue243

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selection);
        getSupportActionBar().hide();
        activity = this;
        mBounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);  //By Kashif feature-issue243

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            int colorString =  extra.getInt("currentSendingColor");
            String pathString =  extra.getString("filePath");
            colorProperty = extra.getString("colorProperty");
            currentColor = colorString;
            filePath = pathString;

            ComponentName aClass = activity.getCallingActivity();
            if (aClass != null) {
                String aClassName = aClass.getClassName();
                Log.d("nameOfActivity",aClassName);
                callingClass = aClass.getClass();
            }
        }

        //region set default colors
        convertRGBtoHSL();
        mainColors = this.getResources().getIntArray(R.array.btn_color);
        //endregion

        redRow = (TableRow) findViewById(R.id.redRow);
        greenSeekBarLayout = (LinearLayout) findViewById(R.id.grrenSeekbar);

        //region set default json objects
        jsonNonHEXRGB = new JSONObject();
        jsonHEXRGB = new JSONObject();
        jsonNonHEXHSL = new JSONObject();
        jsonHEXHSL = new JSONObject();
        //endregion

        seekbars_layout = (LinearLayout) findViewById(R.id.seekbars_layout);

        //region RGB seekbars
        layout_RGB_seekBars = (LinearLayout) findViewById(R.id.layout_RGB_seekBars);

        seekbar_red = (SeekBar) findViewById(R.id.seekbar_red);
        seekbar_green = (SeekBar) findViewById(R.id.seekbar_green);
        seekbar_blue = (SeekBar) findViewById(R.id.seekbar_blue);
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateRGBSeekBarCurrentColor();
            }
        };

        seekbar_red.setMax(255);
        seekbar_green.setMax(255);
        seekbar_blue.setMax(255);

        seekbar_red.setOnSeekBarChangeListener(seekBarChangeListener);
        seekbar_green.setOnSeekBarChangeListener(seekBarChangeListener);
        seekbar_blue.setOnSeekBarChangeListener(seekBarChangeListener);
        //endregion

        //region brightness seekbar
        progress_textView = (TextView) findViewById(R.id.progress_textView);
        brightness_seekBar = (SeekBar) findViewById(R.id.brightness_seekBar);
        brightness_seekBar.setMax(125);
        brightness_seekBar.setProgress(100);
        brightness_seekBar.incrementProgressBy(1);
        brightness_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currentAlpha = progress*2;
                progress_textView.setText(String.valueOf(progress)+"%");

                for (int  colorLoop = 0; colorLoop < btnIdArray.length; colorLoop++)
                {
                    final int b = colorLoop;
                    btn [b] = (Button)findViewById(btnIdArray[b]);
                    btn[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
                    paletteColorsBtnBgShape[b]= (GradientDrawable) btn[b].getBackground();
                    paletteColorsBtnBgShape[b].setColor(mainColors[b]);
                    paletteColorsBtnBgShape[b].setAlpha(currentAlpha);
                }
                if (imageFilePath != null)
                {
                    for (int  colorLoop = 0; colorLoop < imageExtractButtonsIdArray.length; colorLoop++)
                    {
                        final int b = colorLoop;
                        imageExtractButtons [b] = (Button)findViewById(imageExtractButtonsIdArray[b]);
                        imageExtractButtons[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
                        extractedColorsBgShape[b]= (GradientDrawable) imageExtractButtons[b].getBackground();
                        extractedColorsBgShape[b].setColor(extractedColors[b]);
                        extractedColorsBgShape[b].setAlpha(currentAlpha);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //endregion

        //region RBG edit texts
        edittext_red = (EditText) findViewById(R.id.edittext_red);
        edittext_green = (EditText) findViewById(R.id.edittext_green);
        edittext_blue = (EditText) findViewById(R.id.edittext_blue);

        EditText.OnEditorActionListener hrgbTextBoxEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    processRGBTextBoxInput(v);
                    return true;
                }
                return false;
            }
        };

        edittext_red.setOnEditorActionListener(hrgbTextBoxEditorActionListener);
        edittext_green.setOnEditorActionListener(hrgbTextBoxEditorActionListener);
        edittext_blue.setOnEditorActionListener(hrgbTextBoxEditorActionListener);

        textview_red = (TextView) findViewById(R.id.textview_red);
        textview_green = (TextView) findViewById(R.id.textview_green);
        textview_blue = (TextView) findViewById(R.id.textview_blue);
        //endregion

        //region HSL seekbars
        layout_HSL_seekBars = (LinearLayout) findViewById(R.id.layout_HSL_seekBars);

        hsl_seekbar_hue = (SeekBar) findViewById(R.id.hsl_seekbar_hue);
        hsl_seekbar_saturation = (SeekBar) findViewById(R.id.hsl_seekbar_saturation);
        hsl_seekbar_lightness = (SeekBar) findViewById(R.id.hsl_seekbar_lightness);

        SeekBar.OnSeekBarChangeListener seekBarHSLChangeListener = new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateHSLSeekBarCurrentColor();
            }
        };

        hsl_seekbar_hue.setMax(360);
        hsl_seekbar_saturation.setMax(100);
        hsl_seekbar_lightness.setMax(100);

        hsl_seekbar_hue.setOnSeekBarChangeListener(seekBarHSLChangeListener);
        hsl_seekbar_saturation.setOnSeekBarChangeListener(seekBarHSLChangeListener);
        hsl_seekbar_lightness.setOnSeekBarChangeListener(seekBarHSLChangeListener);
        //endregion

        //region HSL edit text
        hsl_edittext_hue = (EditText) findViewById(R.id.hsl_edittext_hue);
        hsl_edittext_saturation = (EditText) findViewById(R.id.hsl_edittext_saturation);
        hsl_edittext_lightness = (EditText) findViewById(R.id.hsl_edittext_lightness);

        EditText.OnEditorActionListener hslTextBoxEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    processHSLTextBoxInput(v);
                    return true;
                }
                return false;
            }
        };

        hsl_edittext_hue.setOnEditorActionListener(hslTextBoxEditorActionListener);
        hsl_edittext_saturation.setOnEditorActionListener(hslTextBoxEditorActionListener);
        hsl_edittext_lightness.setOnEditorActionListener(hslTextBoxEditorActionListener);

        //endregion

        //region Modal Buttons
        hslBtn = (Button) findViewById(R.id.hslBtn);
        hslBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_HSL_seekBars.setVisibility(View.VISIBLE);
                layout_RGB_seekBars.setVisibility(View.GONE);

                colorModel = "HSL";

                redRow.setPadding(100,0,0,0);
                greenSeekBarLayout.setPadding(25,0,0,0);

                hslBtn.setBackground(getResources().getDrawable(R.drawable.model_fill_borderview));
                hslBtn.setTextColor(Color.WHITE);

                rgbBtn.setBackground(getResources().getDrawable(R.drawable.model_borderview));
                rgbBtn.setTextColor(getResources().getColor(R.color.text_color));

                convertRGBtoHSL();
                setSelectShowColorBackground();
                if (isHex)
                {
                    String hexColor = Integer.toHexString(currentColor).substring(2);
                    setHEXHSLColors(hexColor);
                    setHEXHSLTextBoxValues();
                    setHEXHSLSeekBarProgress();
                }
                else
                {
                    setHSLColors();
                    setHSLTextBoxValues();
                    setHSLSeekBarProgress();
                }

            }
        });

        rgbBtn = (Button) findViewById(R.id.rgbBtn);
        rgbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colorModel = "RGB";

                redRow.setPadding(0,0,0,0);
                greenSeekBarLayout.setPadding(0,0,0,0);

                rgbBtn.setBackground(getResources().getDrawable(R.drawable.model_fill_borderview));
                rgbBtn.setTextColor(Color.WHITE);

                hslBtn.setBackground(getResources().getDrawable(R.drawable.model_borderview));
                hslBtn.setTextColor(getResources().getColor(R.color.text_color));

                layout_RGB_seekBars.setVisibility(View.VISIBLE);
                layout_HSL_seekBars.setVisibility(View.GONE);

                setSelectShowColorBackground();

                if (isHex)
                {
                    String hexColor = Integer.toHexString(currentColor).substring(2);
                    setHEXRGBColors(hexColor);
                    setHEXRGBTextBoxValue();
                    setHEXRGBSeekBarProgress();
                }
                else
                {
                    setRGBColors(currentColor);
                    setRGBTextBoxValue();
                    setRGBSeekBarProgress();
                }
            }
        });
        //endregion

        //region switch HEX
        switchHex = (Switch) findViewById(R.id.switchHex);
        switchHex.setChecked(false);
        switchHex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    isHex = true;
                    if (colorModel.equals("RGB"))
                    {
                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        setHEXRGBColors(hexColor);
                        setHEXRGBTextBoxValue();
                        setHEXRGBSeekBarProgress();
                    }
                    else
                    {
                        colorModel = "HSL";

                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        setHEXHSLColors(hexColor);
                        setHEXHSLTextBoxValues();
                        setHEXHSLSeekBarProgress();
                    }

                }
                else
                {
                    isHex = false;
                    if (colorModel.equals("RGB"))
                    {
                        setRGBColors(currentColor);
                        setRGBTextBoxValue();
                        setRGBSeekBarProgress();
                    }
                    else
                    {
                        colorModel = "HSL";

                        setHSLColors();
                        setHSLTextBoxValues();
                        setHSLSeekBarProgress();
                    }
                }
            }
        });
        //endregion

        selectColorShow = (ImageButton) findViewById(R.id.select_color_show);

        //region Color button click
        for (int i=0; i<btnIdArray.length; i++) {
            final int b = i;
            btn [b] = (Button)findViewById(btnIdArray[b]); // Fetch the view id from array
            btn[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
            paletteColorsBtnBgShape[b]= (GradientDrawable) btn[b].getBackground();
            paletteColorsBtnBgShape[b].setColor(mainColors[b]);
            btn [b].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    btn[b].startAnimation(mBounceAnimation);  //By Kashif feature-issue243
                    currentColor = setRGBColorAlpha(mainColors[b],currentAlpha) ;
                    if (colorModel.equals("RGB"))
                    {
                        setSelectShowColorBackground();
                        if (isHex){

                            String hexColor = Integer.toHexString(currentColor).substring(2);
                            setHEXRGBColors(hexColor);
                            setHEXRGBTextBoxValue();
                            setHEXRGBSeekBarProgress();
                        }
                        else {
                            setRGBColors(currentColor);
                            setRGBTextBoxValue();
                            setRGBSeekBarProgress();
                        }
                        sendCurrentColor();  //By Kashif bug-issue243
                    }
                    else
                    {
                        convertRGBtoHSL();
                        setSelectShowColorBackground();

                        if (isHex){

                            String hexColor = Integer.toHexString(currentColor).substring(2);
                            setHEXHSLColors(hexColor);
                            setHEXHSLTextBoxValues();
                            setHEXHSLSeekBarProgress();
                        }
                        else {
                            setHSLColors();
                            setHSLTextBoxValues();
                            setHSLSeekBarProgress();
                        }
                        sendCurrentColor();  //By Kashif bug-issue243
                    }
                }
            });
        }
        //endregion

        edit_color = (ImageView) findViewById(R.id.edit_color);
        edit_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (seekbars_layout.getVisibility() == View.GONE)
               {
                   seekbars_layout.setVisibility(View.VISIBLE);
               }
               else
               {
                   seekbars_layout.setVisibility(View.GONE);
               }
            }
        });
        color_dice = (ImageView) findViewById(R.id.color_dice);
        color_dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int diceColor = Color.argb(currentAlpha, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                currentColor = setRGBColorAlpha(diceColor,currentAlpha) ;
                if (colorModel.equals("RGB"))
                {
                    setSelectShowColorBackground();

                    if (isHex){

                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        setHEXRGBColors(hexColor);
                        setHEXRGBTextBoxValue();
                        setHEXRGBSeekBarProgress();
                    }
                    else {
                        setRGBColors(currentColor);
                        setRGBTextBoxValue();
                        setRGBSeekBarProgress();
                    }
                }
                else
                {
                    convertRGBtoHSL();
                    setSelectShowColorBackground();

                    if (isHex){

                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        setHEXHSLColors(hexColor);
                        setHEXHSLTextBoxValues();
                        setHEXHSLSeekBarProgress();
                    }
                    else {
                        setHSLColors();
                        setHSLTextBoxValues();
                        setHSLSeekBarProgress();
                    }
                }
                //region to show and hide editcolor
                if (seekbars_layout.getVisibility() == View.GONE)
                {
                    seekbars_layout.setVisibility(View.VISIBLE);
                }
                if( color_palette_layout.getVisibility() == View.GONE)
                {
                    color_palette_layout.setVisibility(View.VISIBLE);
                    favorite_palette_layout.setVisibility(View.GONE);
                    isFavorite = false;
                    isFavoriteEdit = false;
                    textDone.setText(getApplicationContext().getResources().getString(R.string.done));
                }
            }
        });

        //region to set default colors for json and HSL
        setRGBColors(currentColor);
        setRGBTextBoxValue();
        convertRGBtoHSL();
        setSelectShowColorBackground();
        setRGBSeekBarProgress();
        //endregion

        //region favorite button click listener
        textDone = (TextView) findViewById(R.id.textDone);
        color_palette_layout = (LinearLayout) findViewById(R.id.color_palette_layout);
        favorite_palette_layout = (LinearLayout) findViewById(R.id.favorite_palette_layout);
        favorite_color_btn = (ImageView) findViewById(R.id.favorite_color_btn);
        favorite_color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colorExtractLayout.setVisibility(View.GONE);
                for (int i=0; i<favtButtonsIdArray.length; i++) {
                    final int b = i;
                    favtButtons [b] = (Button)findViewById(favtButtonsIdArray[b]); // Fetch the view id from array
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("SPFavoriteColor", Context.MODE_PRIVATE);
                    if (preferences.contains(favtButtonsIdArray[b]+"_id") && preferences.getInt(favtButtonsIdArray[b]+"_colorID",0) !=0)
                    {
                        favtButtonsShapeColor = (GradientDrawable) favtButtons[b].getBackground();
                        favtButtonsShapeColor.setColor(preferences.getInt(favtButtonsIdArray[b]+"_colorID",0));
                    }
                    else
                    {
                        favtButtons[b].setBackground(getResources().getDrawable(R.drawable.favt_button_logo));
                    }

                }

                color_palette_layout.setVisibility(View.GONE);
                favorite_palette_layout.setVisibility(View.VISIBLE);
                textDone.setText(getApplicationContext().getResources().getString(R.string.edit));
                isFavorite = true;
                isFavoriteEdit = false;
            }
        });
        //endregion

        //region favourite colors button click
            for (int i=0; i<favtButtonsIdArray.length; i++) {
                final int b = i;
                favtButtons [b] = (Button)findViewById(favtButtonsIdArray[b]); // Fetch the view id from array
                favtButtons[b].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favtButtons[b].startAnimation(mBounceAnimation);  //By Kashif feature-issue243
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SPFavoriteColor", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        if (isFavorite && !isFavoriteEdit)
                        {
                            int sendFavColor = preferences.getInt(favtButtonsIdArray[b]+"_colorID",0);
                           if (sendFavColor ==0)
                           {
//                               sendCurrentColor();
                           }
                           else
                           {
                               favoriteColor = sendFavColor;
                               sendFavoriteColor(sendFavColor);
                               sendCurrentColor();  //By Kashif bug-issue243
                           }
                        }
                        else if (isFavorite && isFavoriteEdit)
                        {
                            if (!preferences.contains(favtButtonsIdArray[b]+"_id"))
                            {
                                editor.putInt(favtButtonsIdArray[b]+"_id",favtButtonsIdArray[b]);
                                editor.putInt(favtButtonsIdArray[b]+"_colorID",currentColor);
                                editor.putBoolean(favtButtonsIdArray[b]+"_isColor",true);
                                editor.commit();
                                favtButtons[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
                                favtButtonsShapeColor = (GradientDrawable) favtButtons[b].getBackground();
                                favtButtonsShapeColor.setColor(currentColor);

                            }
                            else if (preferences.contains(favtButtonsIdArray[b]+"_id") && preferences.getInt(favtButtonsIdArray[b]+"_colorID",0) ==0)
                            {
                                editor.putInt(favtButtonsIdArray[b]+"_colorID",currentColor);
                                editor.putBoolean(favtButtonsIdArray[b]+"_isColor",true);
                                editor.commit();
                                favtButtons[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
                                favtButtonsShapeColor = (GradientDrawable) favtButtons[b].getBackground();
                                favtButtonsShapeColor.setColor(currentColor);

                            }
                            else
                            {
                                editor.putInt(favtButtonsIdArray[b]+"_colorID",0);
                                editor.putBoolean(favtButtonsIdArray[b]+"_isColor",false);
                                editor.commit();
                                favtButtons[b].setBackground(getResources().getDrawable(R.drawable.favt_button_logo));
                                sendCurrentColor();  //By Kashif bug-issue243

                            }
                        }
                    }
                });
            }
            //endregion

        colorExtractLayout = (TableRow) findViewById(R.id.colorExtractLayout);
            if (filePath.length() > 0)
            {
                this.imageFilePath = filePath;
            }

        if (imageFilePath != null && !imageFilePath.equals(""))
        {
            imageColorExtract(imageFilePath);
//            viewSatVal.getLayoutParams().width = 690;   //By Kasshif feature-issue28
            colorExtractLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            colorExtractLayout.setVisibility(View.GONE);
        }

    }

    //region RGB methods
    void setRGBColors(int selectedColor)
    {
        int red = Color.red(selectedColor);
        int green = Color.green(selectedColor);
        int blue = Color.blue(selectedColor);
        try {
            jsonNonHEXRGB.put("RED",red);
            jsonNonHEXRGB.put("GREEN",green);
            jsonNonHEXRGB.put("BLUE",blue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setRGBTextBoxValue()
    {
        try {
            edittext_red.setText(jsonNonHEXRGB.getInt("RED")+"");
            edittext_green.setText(jsonNonHEXRGB.getInt("GREEN")+"");
            edittext_blue.setText(jsonNonHEXRGB.getInt("BLUE")+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setRGBSeekBarProgress()
    {
        try {
            seekbar_red.setProgress(Integer.parseInt(jsonNonHEXRGB.getInt("RED")+""));
            seekbar_green.setProgress(Integer.parseInt(jsonNonHEXRGB.getInt("GREEN")+""));
            seekbar_blue.setProgress(Integer.parseInt(jsonNonHEXRGB.getInt("BLUE")+""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateRGBSeekBarCurrentColor()
    {
        seekR = seekbar_red.getProgress();
        seekG = seekbar_green.getProgress();
        seekB = seekbar_blue.getProgress();
        int bgColor = Color.argb(currentAlpha, seekR, seekG, seekB);
        currentColor = bgColor;
        setSelectShowColorBackground();
        if (isHex)
        {
            setRGBColors(currentColor);
            String hexColor = Integer.toHexString(currentColor).substring(2);
            setHEXRGBColors(hexColor);
            setHEXRGBTextBoxValue();
        }
        else {
            setRGBColors(currentColor);
            setRGBTextBoxValue();
        }
    }
    private int setRGBColorAlpha(int color,int alpha){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);


        return Color.argb(alpha, red, green, blue);
    }
    private void processRGBTextBoxInput(TextView v)
    {
        String check=v.getText().toString();
        if(check.length()!=0 && !check.equals("-")){
            if (isHex) {
                if (check.length() <= 3 && check.length() > 0 && !check.equals("\n")) {
                    char chr1 = 0;
                    char chr2 = 0;
                    boolean isValidChr = false;
                    if (!check.substring(0, 1).equals("")) {
                        chr1 = check.charAt(0);

                    }
                    if (check.length() == 2) {
                        chr2 = check.charAt(1);
                    }
                    for (int i = 0; i < hexCharactersArray.length; i++) {
                        if (chr1 == hexCharactersArray[i] && chr1 != 0) {
                            //convert hex color character into rgb and set seek bar and text box

                        } else {
                        }

                        if (chr2 != 0) {
                            char[] stringToCharArray = check.toCharArray();

                            for (int k = 0; k < stringToCharArray.length; k++) {
                                char currentChr = stringToCharArray[k];
                                for (int l = 0; l < hexCharactersArray.length; l++) {
                                    if (Character.toUpperCase(currentChr) == hexCharactersArray[l]) {
                                        isValidChr = true;
                                        break;
                                    } else {
                                        isValidChr = false;
                                    }
                                }
                            }

                        } else {
                        }


                    }

                    if (isValidChr) {

                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        String substr = hexColor.substring(0, 2);
                        int id = v.getId();
                        if (id == R.id.edittext_red) {
                            substr = hexColor.substring(0, 2);

                        } else if (id == R.id.edittext_green) {
                            substr = hexColor.substring(2, 4);

                        } else if (id == R.id.edittext_blue) {
                            substr = hexColor.substring(4, 6);
                        }


                        CharSequence charSequence = substr;
                        CharSequence chToCharSeq = check;
                        String newHEXColor = hexColor.replace(charSequence, chToCharSeq);
                        Log.d("RGB Color", newHEXColor);

                        int newRGB = Color.parseColor("#" + newHEXColor);
                        int finalRGB = setRGBColorAlpha(newRGB, currentAlpha);
                        currentColor = finalRGB;
                        convertRGBtoHSL();
                        setHEXRGBColors(newHEXColor);
                        setHEXRGBSeekBarProgress();
                        setSelectShowColorBackground();

                    }
                } else {
                    v.setText("");
                }
            }
            else {
                int newValue = Integer.parseInt(check);
                int id = v.getId();
                if (newValue > 255) {

                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.value_range), Toast.LENGTH_SHORT).show();
                    v.setText("255");
                }
                else if (newValue < 0)
                {
                    v.setText("0");
                }
                else {


                    int red = 0, green = 0, blue = 0;
                    if (id == R.id.edittext_red) {
                        red = newValue;
                        green = Color.green(currentColor);
                        blue = Color.blue(currentColor);

                    } else if (id == R.id.edittext_green) {
                        red = Color.red(currentColor);
                        green = newValue;
                        blue = Color.blue(currentColor);


                    } else if (id == R.id.edittext_blue) {
                        red = Color.red(currentColor);
                        green = Color.green(currentColor);
                        blue = newValue;
                    }


                    int finalRGB = createNewRGBColor(red,green,blue);
                    currentColor = finalRGB;
                    convertRGBtoHSL();
                    setRGBColors(currentColor);
                    setRGBSeekBarProgress();
                    setSelectShowColorBackground();

                }
            }
        }
        else
        {
            Toast.makeText(ColorSelectionActivity.this,"Invalid Value",Toast.LENGTH_SHORT).show();
        }
    }

    private int createNewRGBColor(int red, int green, int blue)
    {
        return Color.argb(currentAlpha,red,green,blue);
    }
    //endregion

    //region RGB HEX methods
    void setHEXRGBColors(String hexColors)
    {
        String redHexColor = hexColors.substring(0,2);
        String greenHexColor = hexColors.substring(2,4);
        String blueHexColor = hexColors.substring(4,6);
        try {
            jsonHEXRGB.put("RED",redHexColor.toUpperCase());
            jsonHEXRGB.put("GREEN",greenHexColor.toUpperCase());
            jsonHEXRGB.put("BLUE",blueHexColor.toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHEXRGBTextBoxValue()
    {
        try {
            edittext_red.setText(jsonHEXRGB.getString("RED"));
            edittext_green.setText(jsonHEXRGB.getString("GREEN"));
            edittext_blue.setText(jsonHEXRGB.getString("BLUE"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHEXRGBSeekBarProgress()
    {
        int red = Color.red(currentColor);
        int green = Color.green(currentColor);
        int blue = Color.blue(currentColor);

        seekbar_red.setProgress(red);
        seekbar_green.setProgress(green);
        seekbar_blue.setProgress(blue);
    }
    //endregion

    //region HSL HEX methods
    void setHEXHSLColors(String hexColors)
    {
        String redHexColor = hexColors.substring(0,2);
        String greenHexColor = hexColors.substring(2,4);
        String blueHexColor = hexColors.substring(4,6);

        try {
            jsonHEXHSL.put("HUE",redHexColor.toUpperCase());
            jsonHEXHSL.put("SAT",greenHexColor.toUpperCase());
            jsonHEXHSL.put("VAL",blueHexColor.toUpperCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHEXHSLTextBoxValues()
    {
        try {
            Log.d("json",jsonHEXHSL.getString("HUE"));
            hsl_edittext_hue.setText(jsonHEXHSL.getString("HUE"));
            hsl_edittext_saturation.setText(jsonHEXHSL.getString("SAT"));
            hsl_edittext_lightness.setText(jsonHEXHSL.getString("VAL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHEXHSLSeekBarProgress()
    {
        int hue = (int) currentColorHSL[0];
        float sat = currentColorHSL[1];
        int saturation = (int) (sat*100);
        float light = currentColorHSL[2];
        int lightness = (int) (light*100);

        hsl_seekbar_hue.setProgress(hue);
        hsl_seekbar_saturation.setProgress(saturation);
        hsl_seekbar_lightness.setProgress(lightness);
    }
    //endregion

    //region HSL methods
    void setHSLColors()
    {
        float hue = currentColorHSL[0];
        float sat = currentColorHSL[1]*100;
        float val = currentColorHSL[2]*100;

        try {
            jsonNonHEXHSL.put("HUE",hue);
            jsonNonHEXHSL.put("SAT",sat);
            jsonNonHEXHSL.put("VAL",val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHSLTextBoxValues()
    {
        try {
            hsl_edittext_hue.setText(jsonNonHEXHSL.getInt("HUE")+"");
            hsl_edittext_saturation.setText(jsonNonHEXHSL.getInt("SAT")+"");
            hsl_edittext_lightness.setText(jsonNonHEXHSL.getInt("VAL")+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setHSLSeekBarProgress()
    {
        try {
            hsl_seekbar_hue.setProgress(Integer.parseInt(jsonNonHEXHSL.getInt("HUE")+""));
            hsl_seekbar_saturation.setProgress(Integer.parseInt(jsonNonHEXHSL.getInt("SAT")+""));
            hsl_seekbar_lightness.setProgress(Integer.parseInt(jsonNonHEXHSL.getInt("VAL")+""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateHSLSeekBarCurrentColor() {
        seekH = hsl_seekbar_hue.getProgress();
        seekS = hsl_seekbar_saturation.getProgress();
        seekL = hsl_seekbar_lightness.getProgress();

        Log.d("currentColorHSL", String.valueOf(currentColorHSL));
        float[] seekHSL = new float[3];
        seekHSL[0] = seekH;
        seekHSL[1] = (float) seekS/100;
        seekHSL[2] = (float) seekL/100;


        int seekHSLColor = ColorUtils.HSLToColor(seekHSL);
        float[] newHSL = new float[3];
        newHSL  = setHSLColorAlpha(seekHSLColor,currentAlpha);
        int newRGB = ColorUtils.HSLToColor(newHSL);
        currentColor = newRGB;
        currentColorHSL = newHSL;
        setSelectShowColorBackground();

        if (isHex)
        {
            String hexColor = Integer.toHexString(currentColor).substring(2);
            setHEXHSLColors(hexColor);
            setHEXHSLTextBoxValues();
        }
        else
        {
            setHSLColors();
            setHSLTextBoxValues();
        }
    }
    private float[] setHSLColorAlpha(int color,int alpha){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        float[] hsv = new float[3];
        int alphaRGBColor = Color.argb(alpha, red, green, blue);
        ColorUtils.RGBToHSL(Color.red(alphaRGBColor),Color.green(alphaRGBColor),Color.blue(alphaRGBColor),hsv);
        currentColorHSL[0] = hsv[0];
        currentColorHSL[1] = hsv[1];
        currentColorHSL[2] = hsv[2];
        return currentColorHSL;
    }
    private void processHSLTextBoxInput(TextView v)
    {
        String check=v.getText().toString();
        if(check.length()!=0 && !check.equals("-")){
            if (isHex) {
                if (check.length() <= 3 && check.length() > 0 && !check.equals("\n")) {
                    char chr1 = 0;
                    char chr2 = 0;
                    boolean isValidChr = false;
                    if (!check.substring(0, 1).equals("")) {
                        chr1 = check.charAt(0);

                    }
                    if (check.length() == 2) {
                        chr2 = check.charAt(1);
                    }
                    for (int i = 0; i < hexCharactersArray.length; i++) {
                        if (chr1 == hexCharactersArray[i] && chr1 != 0) {
                            //convert hex color character into rgb and set seek bar and text box

                        } else {
                        }

                        if (chr2 != 0) {
                            char[] stringToCharArray = check.toCharArray();

                            for (int k = 0; k < stringToCharArray.length; k++) {
                                char currentChr = stringToCharArray[k];
                                for (int l = 0; l < hexCharactersArray.length; l++) {
                                    if (Character.toUpperCase(currentChr) == hexCharactersArray[l]) {
                                        isValidChr = true;
                                        break;
                                    } else {
                                        isValidChr = false;
                                    }
                                }
                            }

                        } else {
                        }


                    }

                    if (isValidChr) {

                        String hexColor = Integer.toHexString(currentColor).substring(2);
                        String substr = hexColor.substring(0, 2);
                        int id = v.getId();
                        if (id == R.id.hsl_edittext_hue) {
                            substr = hexColor.substring(0, 2);

                        } else if (id == R.id.hsl_edittext_saturation) {
                            substr = hexColor.substring(2, 4);

                        } else if (id == R.id.hsl_edittext_lightness) {
                            substr = hexColor.substring(4, 6);
                        }


                        CharSequence charSequence = substr;
                        CharSequence chToCharSeq = check;
                        String newHEXColor = hexColor.replace(charSequence, chToCharSeq);
                        Log.d("HEX Color", newHEXColor);

                        int newRGB = Color.parseColor("#" + newHEXColor);
                        float[] newHSL = new float[3];
                        newHSL = setHSLColorAlpha(newRGB, currentAlpha);
                        int finalRGB = ColorUtils.HSLToColor(newHSL);
                        currentColor = finalRGB;
                        currentColorHSL = newHSL;
                        setHEXHSLColors(newHEXColor);
                        setHEXHSLSeekBarProgress();
                        setSelectShowColorBackground();
                    }
                } else {
                    v.setText("");
                }
            }
            else {
                int newValue = Integer.parseInt(check);
                int id = v.getId();
                if (newValue > 360 && id == R.id.hsl_edittext_hue) {

                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.value_range1), Toast.LENGTH_SHORT).show();
                    v.setText("360");
                }
                else if (newValue > 100 && id != R.id.hsl_edittext_hue ) {

                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.value_range2), Toast.LENGTH_SHORT).show();
                    v.setText("100");
                }
                else if (newValue < 0)
                {
                    v.setText("0");
                }
                else {


                    if (id == R.id.hsl_edittext_hue) {
                        currentColorHSL[0] = (float) newValue;

                    } else if (id == R.id.hsl_edittext_saturation) {
                        currentColorHSL[1] = (float) newValue/100;

                    } else if (id == R.id.hsl_edittext_lightness) {
                        currentColorHSL[2] = (float) newValue/100;
                    }


                    int newRGB = ColorUtils.HSLToColor(currentColorHSL);
                    float[] newHSL = new float[3];
                    newHSL = setHSLColorAlpha(newRGB, currentAlpha);
                    int finalRGB = ColorUtils.HSLToColor(newHSL);
                    currentColor = finalRGB;
                    currentColorHSL = newHSL;
                    setHSLColors();
                    setHSLSeekBarProgress();
                    setSelectShowColorBackground();

                }
            }
        }
        else
        {
            Toast.makeText(ColorSelectionActivity.this,"Invalid Value",Toast.LENGTH_SHORT).show();
        }
    }
    private void convertRGBtoHSL(){
        ColorUtils.colorToHSL(currentColor,currentColorHSL);
    }
    //endregion

    public void helpClicked(View v){

        if(Utils.hasConnection(ColorSelectionActivity.this)) {

            Intent intent = new Intent(ColorSelectionActivity.this, HelpActivity.class);
            startActivity(intent);
        }
        else{

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_msg), Toast.LENGTH_LONG).show();
        }
    }

    public void doneClicked(View v)
    {
        if (isFavorite && !isFavoriteEdit)
        {
            isFavoriteEdit = true;
            textDone.setText(getApplicationContext().getResources().getString(R.string.done));
        }
        else if (isFavorite && isFavoriteEdit)
        {
            isFavoriteEdit = false;
//            sendCurrentColor();
            textDone.setText(getApplicationContext().getResources().getString(R.string.edit));
        }
        else
        {
            sendCurrentColor();
        }
    }

    private void sendCurrentColor()
    {
        Intent intent = new Intent(this,callingClass);
        intent.putExtra("responseColor",currentColor);
        intent.putExtra("colorProperty",colorProperty);
        setResult(RESULT_OK,intent);
        finish();
    }
    private void sendFavoriteColor(int sendFavColor)
    {
        Intent intent = new Intent(this,callingClass);
        intent.putExtra("responseColor",sendFavColor);
        intent.putExtra("colorProperty",colorProperty);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void cancelClicked(View v){
        isFavoriteEdit = false;
        isFavorite = false;
        finish();
    }

    private void setSelectShowColorBackground()
    {
        selectColorShowBgShape = (GradientDrawable) selectColorShow.getBackground();
        selectColorShowBgShape.setColor(currentColor);
    }

    private void setSelectShowFavoriteColorBackground()
    {
        favtButtonsShapeColor = (GradientDrawable) selectColorShow.getBackground();
        favtButtonsShapeColor.setColor(currentColor);
    }


    private void imageColorExtract(String imageFilePath) {
        File image = new File(imageFilePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        //region By SJ issue # 241
        try {
            Palette.from( bitmap ).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch dominantSwatch = palette.getDominantSwatch();

                    extractedColors[0] = palette.getDarkMutedColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorPrimaryDark));
                    extractedColors[1] = palette.getDarkVibrantColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorPrimary));
                    extractedColors[2] = palette.getLightMutedColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorAccent));
                    extractedColors[3] = palette.getLightVibrantColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorPrimaryDark));
                    extractedColors[4] = palette.getVibrantColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorPrimary));
                    extractedColors[5] = palette.getMutedColor(ColorSelectionActivity.this.getResources().getColor(R.color.colorAccent));

                    try {
                        colorExtractor.put("Light Vibrant",extractedColors[0]);
                        colorExtractor.put("Vibrant",extractedColors[1]);
                        colorExtractor.put("Dark Vibrant",extractedColors[2]);
                        colorExtractor.put("Light Muted",extractedColors[3]);
                        colorExtractor.put("Muted",extractedColors[4]);
                        colorExtractor.put("Dark Muted",extractedColors[5]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i<imageExtractButtonsIdArray.length; i++)
                    {
                        final int b = i;
                        imageExtractButtons[b] = (Button)findViewById(imageExtractButtonsIdArray[b]); // Fetch the view id from array
                        imageExtractButtons[b].setBackground(getResources().getDrawable(R.drawable.border_textview));
                        extractedColorsBgShape[b]= (GradientDrawable) imageExtractButtons[b].getBackground();
                        extractedColorsBgShape[b].setColor(extractedColors[b]);
                        imageExtractButtons[b].setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                currentColor = setRGBColorAlpha(extractedColors[b],currentAlpha) ;
                                if (colorModel.equals("RGB"))
                                {
                                    setSelectShowColorBackground();
                                    if (isHex){

                                        String hexColor = Integer.toHexString(currentColor).substring(2);
                                        setHEXRGBColors(hexColor);
                                        setHEXRGBTextBoxValue();
                                        setHEXRGBSeekBarProgress();
                                    }
                                    else {
                                        setRGBColors(currentColor);
                                        setRGBTextBoxValue();
                                        setRGBSeekBarProgress();
                                    }
                                }
                                else
                                {
                                    convertRGBtoHSL();
                                    setSelectShowColorBackground();

                                    if (isHex){

                                        String hexColor = Integer.toHexString(currentColor).substring(2);
                                        setHEXHSLColors(hexColor);
                                        setHEXHSLTextBoxValues();
                                        setHEXHSLSeekBarProgress();
                                    }
                                    else {
                                        setHSLColors();
                                        setHSLTextBoxValues();
                                        setHSLSeekBarProgress();
                                    }
                                }
                            }
                        });
                    }

                }

            });
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        //endregion
    }

}