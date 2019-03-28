abstract class BaseViewModel : ViewModel() {
    protected var disposable = CompositeDisposable()

    enum class UI_STATE {
        CONTENT,
        PROGRESS,
        NETWORK_ERROR,
        SERVER_ERROR
    }

    val mApiService: ApiService = ApiModule.getApiService();
    private var uiStateLiveData: MutableLiveData<UI_STATE>? = null

    fun getUiStateLiveData(): MutableLiveData<UI_STATE> {
        if (uiStateLiveData == null) {
            uiStateLiveData = MutableLiveData<UI_STATE>().apply {
                //Need to confirm about this removal
                uiStateLiveData?.value = UI_STATE.CONTENT
            }
        }
        return uiStateLiveData!!
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose();
    }
}


public abstract class BaseRecyclerViewFragment<V> extends BaseFragment implements View.OnClickListener {

    private EndlessRecyclerViewScrollListener mScrollListener;
    public ApiService mApiService;
    private List<V> mDataList;
    private static final String TAG = BaseRecyclerViewFragment.class.getSimpleName();
    private int currPageNumber;

    public BaseRecyclerViewFragment() {
    }

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.progressView)
    LinearLayout mProgressView;

    @BindView(R.id.networkError)
    LinearLayout mNetworkErrorView;

    @BindView(R.id.ServerError)
    LinearLayout mServerErrorView;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.containerView)
    protected View mMainContainer;

    @BindView(R.id.fragment_message)
    protected TextView mMessageView;

    @BindView(R.id.headerContainer)
    protected FrameLayout mHeaderContainer;

    protected FrameLayout mFooterContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiService = ApiModule.getApiService();
        mDataList = new ArrayList<>();

        if (getItemDecoration() != null) {
            mRecyclerView.addItemDecoration(getItemDecoration());
        }
        LinearLayoutManager layoutManager = getLayoutManager();
        mRecyclerView.setLayoutManager(layoutManager);
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
               /* if (page == 1) {
                    return;
                }*/
                if (page == 0) {
                    refreshData();
                } else {
                    fetchDataFromServer(page);
                }
            }
        };

        mFooterContainer = view.findViewById(R.id.footerContainer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRecyclerView.setAdapter(getAdapter());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    protected void removeFooterView() {
        mScrollListener.setFooterCount(0);
        getAdapter().setFooterEnabled(false);
        getAdapter().notifyItemRemoved(mDataList.size());
        getAdapter().notifyDataSetChanged();
    }

    public void addFooterView() {
        mScrollListener.setFooterCount(1);
        getAdapter().setFooterEnabled(true);
//        getAdapter().notifyDataSetChanged();
//        getAdapter().notifyItemInserted(mDataList.size());
    }

    protected void showTitle(@StringRes int titleRes) {
        if (title == null)
            return;
        title.setTextColor(getResources().getColor(R.color.white));
        title.setText(getString(titleRes));
        title.setVisibility(View.VISIBLE);
    }

    public void addHeaderView(int layoutId) {
        if (mHeaderContainer == null)
            throw new NullPointerException("R.id.headerContainer is missing for mHeaderContainer");
        getLayoutInflater().inflate(layoutId, mHeaderContainer);
    }

    public void addFooterView(int layoutId) {
        if (mFooterContainer == null)
            throw new NullPointerException("R.id.footerContainer is missing for mFooterContainer");
        getLayoutInflater().inflate(layoutId, mFooterContainer);
    }

    protected abstract void fetchDataFromServer(int page);

    abstract public com.tictok.tictokgame.Base.BaseAdapter getAdapter();

    protected RecyclerView.ItemDecoration getItemDecoration() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = getResources().getDrawable(R.drawable.recyclerview_divider);
        dividerDrawable.setAlpha(50);
        dividerItemDecoration.setDrawable(dividerDrawable);
        return dividerItemDecoration;
    }

    protected LinearLayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        return layoutManager;
    }


    public void showNetworkError() {
        mMainContainer.setVisibility(View.GONE);
        mNetworkErrorView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        mServerErrorView.setVisibility(View.GONE);
        View retry = mNetworkErrorView.findViewById(R.id.retry);
        retry.setOnClickListener(this);
    }

    public void showServerError(String message) {
        mMainContainer.setVisibility(View.GONE);
        mNetworkErrorView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mServerErrorView.setVisibility(View.VISIBLE);
        View retry = mServerErrorView.findViewById(R.id.retry);
        retry.setOnClickListener(this);
    }

    public void showContentView() {
        mNetworkErrorView.setVisibility(View.GONE);
        mServerErrorView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mMainContainer.setVisibility(View.VISIBLE);
    }

    public void showProgressView() {
        mNetworkErrorView.setVisibility(View.GONE);
        mServerErrorView.setVisibility(View.GONE);
        mMainContainer.setVisibility(View.GONE);
        mMessageView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void showMessageView(String message){
        mNetworkErrorView.setVisibility(View.GONE);
        mServerErrorView.setVisibility(View.GONE);
        mMainContainer.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText(message);
    }

    public void addData(ArrayList<V> data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        final int positionStart = mDataList.size();
        if (data != null) {
            mDataList.addAll(data);
        }
        getAdapter().notifyItemRangeChanged(positionStart, getAdapter().getItemCount() - positionStart);
    }

    public void refreshData() {
        mDataList.clear();
        getAdapter().notifyDataSetChanged();
        fetchDataFromServer(0);
    }

    public List<V> getData() {
        return mDataList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry:
                refreshData();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        if (getAdAdapter() != null) {
            getAdAdapter().destroyViews();
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
