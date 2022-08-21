package com.cumt.musicserver.service;

import com.cumt.musicserver.util.Result;

public interface ISearchService {
    Result searchGetAll(String text);

    Result searchContent(String text);
}
