package io.github.mbarre;

import io.github.mbarre.drhfpnc.sdk.Category;
import io.github.mbarre.drhfpnc.sdk.Sector;
import io.github.mbarre.drhfpnc.sdk.VacantPostsServiceWrapper;

import java.io.IOException;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {


        try {
            VacantPostsServiceWrapper.filterAVP(Sector.TOUTES, Category.A, Arrays.asList(new String[]{"chef", "conseiller"}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
