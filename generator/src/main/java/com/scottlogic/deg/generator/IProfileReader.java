package com.scottlogic.deg.generator;

import com.scottlogic.deg.schemas.common.BaseProfile;

public interface IProfileReader
{
    Profile read(BaseProfile profileDto);
}
