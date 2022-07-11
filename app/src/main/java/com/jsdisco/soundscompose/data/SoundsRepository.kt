package com.jsdisco.soundscompose.data

import android.util.Log
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Interval
import com.jsdisco.soundscompose.data.models.SoundResource

class SoundsRepository {

    val soundResources: List<SoundResource> = loadSounds()

    val intervals: List<Interval> = loadIntervals()

    private fun loadSounds(): List<SoundResource> {
        return listOf(
            SoundResource("c2",48 , R.raw.note48),
            SoundResource("db2", 49, R.raw.note49),
            SoundResource("d2", 50, R.raw.note50),
            SoundResource("eb2", 51, R.raw.note51),
            SoundResource("e2", 52, R.raw.note52),
            SoundResource("f2", 53, R.raw.note53),
            SoundResource("gb2", 54, R.raw.note54),
            SoundResource("g2",55, R.raw.note55),
            SoundResource("ab2",56, R.raw.note56),
            SoundResource("a2",57, R.raw.note57),
            SoundResource("bb2",58, R.raw.note58),
            SoundResource("b2",59, R.raw.note59),
            SoundResource("c3",60, R.raw.note60),
            SoundResource("db3",61, R.raw.note61),
            SoundResource("d3",62, R.raw.note62),
            SoundResource("eb3",63, R.raw.note63),
            SoundResource("e3",64, R.raw.note64),
            SoundResource("f3", 65,R.raw.note65),
            SoundResource("gb3",66, R.raw.note66),
            SoundResource("g3",67, R.raw.note67),
            SoundResource("ab3",68, R.raw.note68),
            SoundResource("a3",69, R.raw.note69),
            SoundResource("bb3", 70,R.raw.note70),
            SoundResource("b3", 71,R.raw.note71),
            SoundResource("c4", 72,R.raw.note72),
        )
    }

    private fun loadIntervals(): List<Interval>{
        return listOf(
            Interval(1, "kleine Sekunde"),
            Interval(2, "große Sekunde"),
            Interval(3, "kleine Terz"),
            Interval(4, "große Terz"),
            Interval(5, "Quarte"),
            Interval(6, "Tritonus"),
            Interval(7, "Quinte"),
            Interval(8, "kleine Sexte"),
            Interval(9, "große Sexte"),
            Interval(10, "kleine Septime"),
            Interval(11, "große Septime"),
            Interval(12, "Oktave")
        )
    }

}