/* Class Name: ND_ToastMessage
 * Version : ND-1.0
 * Data: 09.19.15
 * CopyWrit:
 *
 */

package com.example.santosh.notedown;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Santosh on 9/19/2015.
 */
public class ND_ToastMessage {
    public static void message (Context c , String msg){

        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();

    }
}
