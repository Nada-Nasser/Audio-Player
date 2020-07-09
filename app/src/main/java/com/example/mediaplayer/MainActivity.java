package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    SeekBar songSeekBar;
    ListView listView;

    ArrayList<SongInfo> songsList;
    ListAdapter listAdapter;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsList = new ArrayList<>();
        songSeekBar = findViewById(R.id.SONG_SEEK_BAR);
        listView = findViewById(R.id.SONG_LIST_VIEW);
        listAdapter = new ListAdapter(songsList);
        listView.setAdapter(listAdapter);
        getSongs();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.stop();
                }

                SongInfo songInfo=songsList.get(position);
                mediaPlayer=new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(songInfo.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    songSeekBar.setMax(mediaPlayer.getDuration());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Make sure there is internet connetion",Toast.LENGTH_LONG).show();
                }


            }
        });

        myThread thread = new myThread();
        thread.start();

    }

    public  void getSongs()
    {
        songsList.clear();
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/002.mp3","البقره - AL BAKRA","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/003.mp3","آل عمران - AAl Emran","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/004.mp3","النساء - AL Nesaa","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/005.mp3","المائده - AL MAEDA","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/006.mp3","الأنعام - AL Anaan","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/007.mp3","الأعراف - AL Araf","Quran","محمد الطبلاوي"));
        songsList.add(new SongInfo("https://server12.mp3quran.net/tblawi/108.mp3","AL Qwther - الكوثر" , "Quran","محمد الطبلاوي"));
        listAdapter.notifyDataSetChanged();
    }

    public void onClickContinueButton(View view)
    {
        mediaPlayer.start();
    }

    public void onClickStopButton(View view)
    {
        mediaPlayer.pause();
    }

    class ListAdapter extends BaseAdapter
    {
        ArrayList<SongInfo> songsList;

        public ListAdapter(ArrayList<SongInfo> songsList) {
            this.songsList = songsList;
        }

        @Override
        public int getCount() {
            return songsList.size();
        }

        @Override
        public Object getItem(int i) {
            return songsList.indexOf(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            LayoutInflater inflater = getLayoutInflater();
            View songsView = inflater.inflate(R.layout.list_item_layout,null);

            final SongInfo songItem = songsList.get(i);

            TextView songNameTextView = songsView.findViewById(R.id.SONG_NAME);
            TextView songTypeTextView = songsView.findViewById(R.id.SONG_TYPE);

            songNameTextView.setText(songItem.getSongName());
            songTypeTextView.setText(songItem.getAlbumName());

            return songsView;
        }
    }

    class myThread extends Thread
    {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int progress = mediaPlayer.getCurrentPosition();
                            songSeekBar.setProgress(progress);
                        }
                    }
                });
            }
        }
    }

}