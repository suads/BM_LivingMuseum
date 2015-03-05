package quad.eswc2014.bm_livingmuseum;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import eswcss.quads.museumpandora.main.Artefact;
import eswcss.quads.museumpandora.main.User;

/**
 * Created by sejdovic on 04.09.2014.
 */

public class ArtifactFragment extends Fragment implements TextToSpeech.OnInitListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    String imgUrl;

    private TextToSpeech tts;
    private ImageButton btnSpeak;
    private Boolean ttsInitDone;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                ttsInitDone=true;
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String output) {
        if(ttsInitDone) {
            tts.setSpeechRate((float) 0.8);
            tts.setPitch((float) 1.5);
            tts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Image ...");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ArtifactFragment newInstance(int sectionNumber) {
        ArtifactFragment fragment = new ArtifactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ArtifactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.artifact_fragment, container, false);

        //final Artefact artefact = cleanArtefact(User.getInstance().getCurrentArtefact());
        Artefact artefact = User.getInstance().getCurrentArtefact();
        User.getInstance().addToHistory(User.getInstance().getCurrentArtefact());
        //final Artefact artefact = new Artefact();
        //artefact.setImageURL("http://www.britishmuseum.org/images/ahow_001_624x352.jpg");
        //artefact.setDescription("Base and lid of the anthropoid wooden inner coffin of Hornedjitef, son of Nekhthorheb, who held a large number of priestly offices, polychrome painted and gilded face, wig, collar and pectoral, winged scarab across breast, body inscribed with vertical registers of painted hieroglyphs, flanked by deities, the rest of the surface is unelaborated, interior of base also decorated.");
        //artefact.setTitle("Mummy of Hornedjitef");

        TextView title = (TextView) rootView.findViewById(R.id.textView);
        if(title!=null){
            title.setText(artefact.getTitle());
        }
        TextView description = (TextView) rootView.findViewById(R.id.description);
        description.setText(artefact.getDescription());
        img = (ImageView)rootView.findViewById(R.id.img);
        new LoadImage().execute(artefact.getImageURL());

        tts = new TextToSpeech(getActivity(), this);
        btnSpeak = (ImageButton) rootView.findViewById(R.id.audioBtn);
        final String test = artefact.getDescription();
        if (btnSpeak!=null) {
            btnSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tts.isSpeaking()) {
                        tts.stop();
                    }
                    else {
                        speakOut(test);
                    }
                }
            });
        }

        ImageButton likeBtn = (ImageButton) rootView.findViewById(R.id.likeBtn);
        likeBtn.setClickable(true);
        if (likeBtn!=null) {
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Like", "clicked");
                    User.getInstance().likes();

                }
            });
        }

        ImageButton dislikeBtn = (ImageButton) rootView.findViewById(R.id.dislikeBtn);
        dislikeBtn.setClickable(true);
        if (dislikeBtn!=null) {
            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Dislike", "clicked");
                    User.getInstance().dislikes();
                    ViewGroup vg = (ViewGroup) getActivity().findViewById(R.id.layoutParent);
                    vg.invalidate();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onDestroy(){
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public Artefact cleanArtefact(Artefact artefact) {
        Artefact cleanedArtefact = new Artefact();

        cleanedArtefact = artefact;

        if(cleanedArtefact.getTitle().isEmpty()){
            cleanedArtefact.setTitle(cleanedArtefact.getObjectType().substring(0,1).toUpperCase()+cleanedArtefact.getObjectType().substring(1));
            if(!cleanedArtefact.getCulture().isEmpty() && cleanedArtefact.getFoundat().isEmpty()){
                cleanedArtefact.setTitle(cleanedArtefact.getTitle() +" from "+cleanedArtefact.getCulture());
            }
            if(!cleanedArtefact.getFoundat().isEmpty() && !cleanedArtefact.getCulture().isEmpty()) {
                cleanedArtefact.setTitle(cleanedArtefact.getTitle() +" from "+cleanedArtefact.getCulture()+", "+cleanedArtefact.getFoundat());
            }
        }
        if(cleanedArtefact.getImageURL().isEmpty()) {
            cleanedArtefact.setImageURL("http://www.britishmuseum.org/images/ahow_001_624x352.jpg");
        }

        return cleanedArtefact;
    }

    public void update() {
        Artefact artefact = User.getInstance().getCurrentArtefact();
        TextView title = (TextView) getActivity().findViewById(R.id.textView);
        if(title!=null){
            title.setText(artefact.getTitle());
        }
        TextView description = (TextView) getActivity().findViewById(R.id.description);
        description.setText(artefact.getDescription());
        img = (ImageView)getActivity().findViewById(R.id.img);
        new LoadImage().execute(artefact.getImageURL());
    }
}