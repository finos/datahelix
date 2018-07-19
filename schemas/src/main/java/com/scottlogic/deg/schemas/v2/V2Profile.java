package com.scottlogic.deg.schemas.v2;

import com.scottlogic.deg.schemas.common.BaseProfile;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

public class V2Profile extends BaseProfile {
    @XmlTransient
    public static final String SchemaVersion = "v2";

    public List<Field> fields = new ArrayList<Field>();

    public V2Profile()
    {
        super(V2Profile.SchemaVersion);
    }
}
