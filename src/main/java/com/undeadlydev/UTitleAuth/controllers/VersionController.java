package com.undeadlydev.UTitleAuth.controllers;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.enums.Versions;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionNew;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionNewes;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionOld;
import com.undeadlydev.UTitleAuth.superclass.NMSReflection;

public class VersionController {

    private final TitleAuth plugin;
    private final NMSReflection reflection;

    public VersionController(TitleAuth plugin) {
        this.plugin = plugin;
        if (Versions.getVersion().esMayorIgual(Versions.v1_20_5)) {
            this.reflection = new NMSReflectionNewes();
        } else if (Versions.getVersion().esMayorIgual(Versions.v1_17_1)) {
            this.reflection = new NMSReflectionNew();
        } else {
            this.reflection = new NMSReflectionOld();
        }
    }

    public NMSReflection getReflection() {
        return reflection;
    }
}