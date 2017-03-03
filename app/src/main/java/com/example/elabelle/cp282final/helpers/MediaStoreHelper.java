package com.example.elabelle.cp282final.helpers;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by elabelle on 3/2/17.
 */

public class MediaStoreHelper {
    public Uri createURI(String type){
        switch (type) {
            case "image":
                return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            case "video":
                return  MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            case "audio":
                return  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        return null;
    }
}
