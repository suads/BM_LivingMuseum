package quad.eswc2014.bm_livingmuseum;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;

import eswcss.quads.museumpandora.main.Artefact;
import eswcss.quads.museumpandora.main.Engine;
import eswcss.quads.museumpandora.main.User;

/**
 * Created by sejdovic on 04.09.2014.
 */

public class POIFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    Button load_img;
    ImageView img;
    Bitmap bitmap;
    String imgUrl;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static POIFragment newInstance(int sectionNumber) {
        POIFragment fragment = new POIFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public POIFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.poi_fragment, container, false);

        TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.poiTable);
        Artefact artefact = new Artefact();

        Engine.getInstance().setArtefacts(new LinkedList<Artefact>());

        String user_location = User.getInstance().getCurrentArtefact().getLocation();
        System.err.println("User Location: "+user_location);

        System.err.println("Recommendations: ");
        String regex = "("+user_location;
        for(String room_nb: Engine.getInstance().getNeighboringRooms(user_location)){
            if(!room_nb.matches("[0-9]+"))
                continue;
            System.err.println("Room Nb: "+room_nb);
            regex+="|"+room_nb;
        }
        regex +=")";

        Engine.getInstance().generateArtefacts("The British Museum: Gallery G" + regex);
        System.err.println("Number of Artefacts: " + Engine.getInstance().getArtefacts().size());
        Collections.sort(Engine.getInstance().getArtefacts());

        int i = 0;
        for(Artefact art: Engine.getInstance().getArtefacts()){
            tableLayout.addView(createRow(art),i);
            i++;
            if(i%3==0){
                break;
            }
        }
        //artefact.setImageURL("http://www.britishmuseum.org/images/ahow_001_624x352.jpg");
        //artefact.setTitle("Mummy of Hornedjitef");

        //for (int i = 0; i <2; i++) {
        //    tableLayout.addView(createRow(artefact), i);
        //}
        return rootView;
    }

    public TableRow createRow(Artefact artefact){
        TableRow tableRow = new TableRow(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(layoutParams);
        tableRow.setBackgroundResource(R.drawable.seperator);
//        tableRow.setGravity(Gravity.CENTER);

        ImageView timeImg = new ImageView(getActivity());
        timeImg.setImageResource(R.drawable.time);
        ImageView recomImg = new ImageView(getActivity());
        recomImg.setImageResource(R.drawable.recommended);
        recomImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //ImageButton crowdedBtn = new ImageButton(getActivity());
        //crowdedBtn.setImageResource(R.drawable.crowded);
        //ImageButton timeBtn = new ImageButton(getActivity());
        //timeBtn.setImageResource(R.drawable.time);
        //ImageButton recomBtn = new ImageButton(getActivity());
        //recomBtn.setImageResource(R.drawable.recommended);

        LinearLayout statusLinearLayout = new LinearLayout(getActivity());
        statusLinearLayout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

        LinearLayout statusRow2 = new LinearLayout(getActivity());
        statusRow2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        statusRow2.addView(timeImg);
        statusLinearLayout.addView(statusRow2);

        LinearLayout statusRow3 = new LinearLayout(getActivity());
        statusRow3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        statusRow3.addView(recomImg);
        statusLinearLayout.addView(statusRow3);

        ImageView imageView = new ImageView(getActivity());
        new DownloadImageTask(imageView).execute(artefact.getImageURL());
        TextView title = new TextView(getActivity());
        title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        String tt = artefact.getTitle();
        if(tt.length()==0)
            tt = artefact.getDescription();
        if(tt.length()==0)
            tt = "No Title available";

        title.setText(tt);

        TextView description = new TextView(getActivity());
        description.setGravity(Gravity.CENTER);

        String text = artefact.getLongSynopsis();
        if(text.length()==0)
            text = artefact.getMediumSynopsis();
        if(text.length()==0)
            text = artefact.getShortSynopsis();
        if(text.length()==0)
            text = artefact.getDescription();
        if(text.length()==0)
            text = "No description available";

        if (text.equals(tt)) {
            description.setText("");
        } else {
            description.setText(text);
        }

        LinearLayout artefactLinearLayout = new LinearLayout(getActivity());
        artefactLinearLayout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

        LinearLayout artefactRow1 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams artefactRow1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        artefactRow1params.gravity=Gravity.CENTER;
        artefactRow1.setLayoutParams(artefactRow1params);
        artefactRow1.addView(imageView);
        artefactLinearLayout.addView(artefactRow1);

        ScrollView sv1 = new ScrollView(getActivity());
        sv1.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT));

        LinearLayout artefactRow2 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams artefactRow2params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        artefactRow2params.gravity= Gravity.CENTER;
        artefactRow2.setLayoutParams(artefactRow2params);
        artefactRow2.addView(title);
        sv1.addView(artefactRow2);

        artefactLinearLayout.addView(sv1);

        ScrollView sv2 = new ScrollView(getActivity());
        sv2.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.MATCH_PARENT));

        LinearLayout artefactRow3 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams artefactRow3params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        artefactRow3params.gravity= Gravity.CENTER;
        artefactRow3.setLayoutParams(artefactRow2params);
        artefactRow3.addView(description);
        sv2.addView(artefactRow3);

        artefactLinearLayout.addView(sv2);

        tableRow.addView(statusLinearLayout);
        tableRow.addView(artefactLinearLayout);

        return tableRow;
    }
}