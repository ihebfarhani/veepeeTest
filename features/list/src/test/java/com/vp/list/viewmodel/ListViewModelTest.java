package com.vp.list.viewmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.mock.Calls;

public class ListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();
    String title = "test";
    int page = 1;
    @Mock
    private Call<SearchResponse> searchResponseCall;
    private ListViewModel viewModel;
    @Mock
    private SearchService searchService;
    @Mock
    private Observer<SearchResult> observer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new ListViewModel(searchService);
        viewModel.observeMovies().observeForever(observer);
        when(searchService.search(anyString(), anyInt())).thenReturn(searchResponseCall);
    }

    @Test
    public void shouldReturnErrorState() {
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(new IOException()));
        viewModel.searchMoviesByTitle(title, page);
        assertThat(viewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.ERROR);
    }

    @Test
    public void shouldReturnInProgressState() {
        viewModel.searchMoviesByTitle(title, page);
        verify(observer).onChanged(argThat(argument -> argument.getListState() == ListState.IN_PROGRESS));
        verify(searchService).search(title, page);
    }


    @Test
    public void searchMoviesByTitle_Success() {
        ListItem item1 = new ListItem("Title1", "", "", "");
        ListItem item2 = new ListItem("Title2", "", "", "");
        List<ListItem> searchItems = Arrays.asList(item1, item2);
        int totalResults = 10;
        SearchResponse searchResponse = new SearchResponse("True");
        searchResponse.setSearch(searchItems);
        searchResponse.setTotalResults(totalResults);
        when(searchService.search(title, page)).thenReturn(searchResponseCall);
        doAnswer(invocation -> {
            Callback<SearchResponse> callback = invocation.getArgument(0);
            callback.onResponse(searchResponseCall, Response.success(searchResponse));
            return null;
        }).when(searchResponseCall).enqueue(any());

        viewModel.searchMoviesByTitle(title, page);

        verify(observer).onChanged(argThat(argument -> argument.getItems().equals(searchItems) &&
                argument.getTotalResult() == totalResults &&
                argument.getListState() == ListState.LOADED));

        verify(searchService).search(title, page);
    }

}