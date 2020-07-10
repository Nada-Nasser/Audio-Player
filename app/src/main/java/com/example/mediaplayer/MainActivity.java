package com.example.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int SONGS_REQUEST_CODE = 123;
    SeekBar songSeekBar;
    ListView listView;

    ArrayList<SongInfo> songsList;
    ListAdapter listAdapter;

    MediaPlayer mediaPlayer;
    int seekValue = 0;
    TextView currentSongTextView;

    int NextSongPos = 0;

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
        currentSongTextView = findViewById(R.id.CURRENT_SONG_ID);

        checkPermission();

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                seekValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mediaPlayer.seekTo(seekValue);
            }
        });

      //  mediaPlayer=new MediaPlayer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                setCurrentSong(position);
            }
        });

        /*
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setCurrentSong(NextSongPos);
            }
        });*/

        myThread thread = new myThread();
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkPermission();
    }

    private void setCurrentSong(int position)
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
        }

        SongInfo songInfo=songsList.get(position);

        NextSongPos = position+1;
        if(NextSongPos>= songsList.size())
            NextSongPos = 0;

        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songInfo.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            currentSongTextView.setText(songInfo.getSongName());
            songSeekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
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
    */

    private void checkPermission()
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                            , SONGS_REQUEST_CODE);
                }

                return;
            }
        }

        getSongs();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == SONGS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongs();
            } else {
                Toast.makeText(getApplicationContext(), "You can not use these feature without location access",
                        Toast.LENGTH_LONG).show();
            }
            return;
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getSongs()
    {
        songsList.clear();
        Uri AllSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selectionMusic = MediaStore.Audio.Media.IS_MUSIC;

        Cursor cursor = getContentResolver().query(AllSongsURI,null,selectionMusic
                ,null,null);

        if(cursor.moveToNext())
        {
            do {
                String songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String songAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));;
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));;

                songsList.add(new SongInfo(songPath,songName,songAlbum,artistName));

            }while (cursor.moveToNext());
        }

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


                            int max = mediaPlayer.getDuration();

                            if(progress >= max)
                            {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                setCurrentSong(NextSongPos);
                            }
                        }
                    }
                });
            }
        }
    }

}