package com.jsdisco.soundscompose.data

import android.content.Context
import android.util.Log
import com.jsdisco.soundscompose.R
import com.jsdisco.soundscompose.data.models.Interval
import com.jsdisco.soundscompose.data.models.Sound
import com.jsdisco.soundscompose.data.models.SoundResource

class SoundsRepository {

    val soundResources: List<SoundResource> = loadSoundResources()

    val intervals: List<Interval> = loadIntervals()

    private fun loadSoundResources(): List<SoundResource> {
        return listOf(
            /*SoundResource("c1", 36, R.raw.note36),
            SoundResource("db1", 37, R.raw.note37),
            SoundResource("d1", 38, R.raw.note38),
            SoundResource("eb1", 39, R.raw.note39),
            SoundResource("e1", 40, R.raw.note40),
            SoundResource("f1", 41, R.raw.note41),
            SoundResource("gb1", 42, R.raw.note42),
            SoundResource("g1", 43, R.raw.note43),
            SoundResource("ab1", 44, R.raw.note44),
            SoundResource("a1", 45, R.raw.note45),
            SoundResource("bb1", 46, R.raw.note46),
            SoundResource("b1", 47, R.raw.note47),*/
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
            SoundResource("db4", 73,R.raw.note73),
            SoundResource("d4", 74,R.raw.note74),
            SoundResource("eb4", 75,R.raw.note75),
            SoundResource("e4", 76,R.raw.note76),
            SoundResource("f4", 77,R.raw.note77),
            SoundResource("gb4", 78,R.raw.note78),
            SoundResource("g4", 79,R.raw.note79),
            SoundResource("ab4", 80,R.raw.note80),
            SoundResource("a4", 81,R.raw.note81),
            SoundResource("bb4", 82,R.raw.note82),
            SoundResource("b4", 83,R.raw.note83),
            SoundResource("c5", 84,R.raw.note84),
            SoundResource("db5", 85,R.raw.note85),
            SoundResource("d5", 86,R.raw.note86),
            SoundResource("eb5", 87,R.raw.note87),
            SoundResource("e5", 88,R.raw.note88),
            SoundResource("f5", 89,R.raw.note89),
            SoundResource("gb5", 90,R.raw.note90),
            SoundResource("g5", 91,R.raw.note91),
            SoundResource("ab5", 92,R.raw.note92),
            SoundResource("a5", 93,R.raw.note93),
            SoundResource("bb5", 94,R.raw.note94),
            SoundResource("b5", 95,R.raw.note95),
            SoundResource("c6", 96,R.raw.note96),
            SoundResource("db6", 97,R.raw.note97),
            SoundResource("d6", 98,R.raw.note98),
            SoundResource("eb6", 99,R.raw.note99),
            SoundResource("e6", 100,R.raw.note100),
            SoundResource("f6", 101,R.raw.note101),
            SoundResource("gb6", 102,R.raw.note102),
            SoundResource("g6", 103,R.raw.note103),
            SoundResource("ab6", 104,R.raw.note104),
            SoundResource("a6", 105,R.raw.note105),
            SoundResource("bb6", 106,R.raw.note106),
            SoundResource("b6", 107,R.raw.note107),
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