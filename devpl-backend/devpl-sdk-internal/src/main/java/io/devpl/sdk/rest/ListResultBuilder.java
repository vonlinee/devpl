package io.devpl.sdk.rest;

import java.util.List;

public interface ListResultBuilder<E> extends RestfulResultBuilder<ListResult<E>, ListResultBuilder<E>> {

    ListResultBuilder<E> setData(List<E> data);

    default ListResultBuilder<E> data(List<E> data) {
        return setData(data);
    }

    default ListResultBuilder<E> pageInfo(PageInfo pageInfo) {
        return setPageInfo(pageInfo);
    }

    ListResultBuilder<E> setPageInfo(PageInfo pageInfo);
}
