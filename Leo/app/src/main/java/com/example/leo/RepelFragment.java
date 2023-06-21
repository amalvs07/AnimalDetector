package com.example.leo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leo.ml.MobilenetV110224Quant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Button selectImage,Capureimage,PredictImageresult;
    TextView text1;
    Bitmap bitmap;
    ImageView imageView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RepelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepelFragment newInstance(String param1, String param2) {
        RepelFragment fragment = new RepelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode==10){
//            if (data!=null){
//                Uri uri=data.getData();
//                try{
//                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
//                    imageView.setImageBitmap(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        else if (requestCode==12){
//            bitmap=(Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_repel, container, false);

        String[] labels=new String[1001];
        int cnt=0;
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getContext().getAssets().open("labels.txt")));
            String line=bufferedReader.readLine();
            while (line!=null){
                labels[cnt]=line;
                cnt++;
                line=bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        selectImage=v.findViewById(R.id.selectItem);
//        Capureimage=v.findViewById(R.id.button2);
        PredictImageresult=v.findViewById(R.id.PreditItem);
        text1=v.findViewById(R.id.Results);
        imageView=v.findViewById(R.id.ViewImageView);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);

            }
        });
        PredictImageresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);


                    // Creates inputs for reference.

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

                    bitmap=Bitmap.createScaledBitmap(bitmap,224,224,true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getActivity());

                    // Runs model inference and gets result.
                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    text1.setText(labels[getMax(outputFeature0.getFloatArray())]+"");

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });


        return v;

    }
    int getMax(float[] arr){
        int max=0;
        for (int i=0;i<arr.length;i++){
            if(arr[i]>arr[max]) max=i;
        }
        return max;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10){
            if (data!=null){
                Uri uri=data.getData();
                try{
                    bitmap= MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),uri);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        else if (requestCode==12){
//            bitmap=(Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
//        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}

