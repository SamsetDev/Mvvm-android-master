//ViewModelClass

class SportsLeagueViewModel : BaseViewModel() {
    private val model = SportsLeagueModel()
    private var leagues = SingleLiveEvent<ArrayList<DataSportsLeague>>()
    private var promoList = SingleLiveEvent<ArrayList<DataPromo>>()

    fun getLeagues(): MutableLiveData<ArrayList<DataSportsLeague>> {
        return leagues
    }

    fun getPromoList(): MutableLiveData<ArrayList<DataPromo>> {
        return promoList
    }

    fun fetchLeagues(pageNo: Int) {
        model.fetchLeagues(pageNo, leagues, promoList, getUiStateLiveData(), disposable)
    }

    fun fetchMyMatches(pageNo: Int, matchType: MatchFragmentType) {
        model.fetchMyMatches(pageNo, matchType.name, leagues, getUiStateLiveData(), disposable)
    }
}


//Fragment

class SportsLeagueFragment : BaseRecyclerViewFragment<DataSportsLeague>(), SportsLeagueAdapter.SportsLeagueAdapterInterface {
    private lateinit var adapter: SportsLeagueAdapter
    private var fragmentType: MatchFragmentType = MatchFragmentType.FEED
    private lateinit var viewModel: SportsLeagueViewModel
    private lateinit var mRefreshPublisher: PublishSubject<Int>
    private var mPromoHelper: PromoCarouselHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getSerializable(MyMatchesFragment.matchesFragmentType) != null) {
            fragmentType = arguments?.getSerializable(MyMatchesFragment.matchesFragmentType) as MatchFragmentType
        }
        viewModel = ViewModelProviders.of(this)
                .get(SportsLeagueFragment::class.java.simpleName + fragmentType.name, SportsLeagueViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crazy_quizzes, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setObservers()
        setupFab()
        showProgressView()
        setupRefreshPublisher();
    }

    override fun onResume() {
        super.onResume()
        mPromoHelper?.startCarousel()
    }

    override fun onPause() {
        super.onPause()
        mPromoHelper?.stopCarousel()
    }

    private fun init() {

        mPromoHelper = PromoCarouselHelper(carouselLayout)
        adapter = SportsLeagueAdapter(activity as Activity, this, fragmentType)

        if (!BuildConfig.is_gold) {
            fabOverlay.visibility = View.GONE
            val params = recycler_view.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, 0);
        }
    }

    override fun getAdapter(): BaseAdapter<DataSportsLeague> {
        return adapter
    }

    private fun setupFab() {
        if (fragmentType != MatchFragmentType.FEED) {
            fabOverlay.visibility = View.GONE
            return
        }

        setupMenu(menuPastWinner, R.drawable.ic_fab_past_winner, R.string.my_matches);
        floatingActionsMenu2.setOnFloatingActionsMenuUpdateListener(object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
            override fun onMenuExpanded() {
                fabOverlay.setOnClickListener() { floatingActionsMenu2.collapse() }
                fabOverlay.setBackgroundColor(context!!.resources.getColor(R.color.primarydark_87))
                fabOverlay.isClickable = true
            }

            override fun onMenuCollapsed() {
                fabOverlay.background = null
                fabOverlay.isClickable = false
            }

        })
    }

    private fun setupMenu(menuView: View, @DrawableRes icon: Int, @StringRes string: Int) {
        var title = menuView.findViewById<TextView>(R.id.title)
        var image = menuView.findViewById<ImageView>(R.id.image)
        title.text = getString(string)
        image.setImageResource(icon)
        setMenuClickListener(menuView)
    }

    private fun setMenuClickListener(menuView: View) {
        var menuListener: View.OnClickListener

        when (menuView.id) {
            R.id.menuPastWinner -> menuListener = View.OnClickListener { showMyMatches() }
            else -> menuListener = View.OnClickListener { }
        }
        menuView.findViewById<TextView>(R.id.title).setOnClickListener(menuListener)
        menuView.findViewById<ImageView>(R.id.image).setOnClickListener(menuListener)
    }

    private fun showMyMatches() {
        if (BuildConfig.is_gold)
            ContainerActivity.startActivity(context, ContainerActivity.FragmentTag.MY_MATCHES);
        else
            LockDialog(activity as Context).Builder().build().show()
        floatingActionsMenu2.collapse()
    }

    private fun setObservers() {
        //Observe League list from server
        viewModel.getLeagues().observe(this, Observer<ArrayList<DataSportsLeague>> { value ->
            if ((super.getData() == null || super.getData().size <= 0) && (value == null || value?.size <= 0)) {
                showMessageView(getEmptyListMessage())
                return@Observer
            }
            value?.let {
                addData(it)
                showContentView()
            }
        })

        //Observe status for Server request from server
        viewModel.getUiStateLiveData().observe(this, Observer<UI_STATE> {
            when (it) {
                UI_STATE.CONTENT -> {
                    removeFooterView()
                    showContentView()
                }
                UI_STATE.PROGRESS -> {
                    addFooterView()
                }
                UI_STATE.NETWORK_ERROR -> {
                    showNetworkError()
                    removeFooterView()
                }
                UI_STATE.SERVER_ERROR -> {
                    showServerError(getString(R.string.server_error_code, 100))
                    removeFooterView()
                }
            }
        })

        //Observe Promos
        if (fragmentType == MatchFragmentType.FEED) {
            viewModel.getPromoList().observe(this, Observer {
                it?.let { promoList ->
                    mPromoHelper?.updateCarouselData(it)
                    mPromoHelper?.startCarousel();
                }
            })
        }
    }

    private fun getEmptyListMessage(): String {
        when (fragmentType) {
            MatchFragmentType.FEED -> return getString(R.string.empty_league_list)
            MatchFragmentType.UPCOMING -> return getString(R.string.empty_upcoming_list)
            MatchFragmentType.LIVE -> return getString(R.string.empty_live_list)
            MatchFragmentType.COMPLETED -> return getString(R.string.empty_completed_list)
        }
    }

    override fun refreshListData() {
        mRefreshPublisher.onNext(1)
    }

    override fun getData(): MutableList<DataSportsLeague> {
        return super.getData()
    }

    override fun fetchDataFromServer(pageNo: Int) {
        when (fragmentType) {
            MatchFragmentType.FEED -> viewModel.fetchLeagues(pageNo)
            else -> {
                viewModel.fetchMyMatches(pageNo, fragmentType)
            }
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

    private fun setupRefreshPublisher() {
        mRefreshPublisher = PublishSubject.create();
        val refreshDisposable = mRefreshPublisher
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    refreshData()
                }
        disposable.add(refreshDisposable);
    }

}

//Adapter

class SportsLeagueAdapter(val activity: Activity, val fragmentInterface: SportsLeagueAdapterInterface,
                          val fragmentType: MatchFragmentType) : BaseAdapter<DataSportsLeague>() {
    override fun getDataList(): MutableList<DataSportsLeague> = fragmentInterface.getData()

    override fun createItemViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_sports_league_match, parent, false)
        return SportsLeagueViewHolder(view);
    }

    override fun bindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is SportsLeagueViewHolder)
            holder bindData position
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is SportsLeagueViewHolder)
            holder.attachToWindow()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is SportsLeagueViewHolder)
            holder.detachFromWindow()
    }

    inner class SportsLeagueViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var counter: Counter? = null

        infix fun bindData(position: Int) {
            var data = dataList[position]

            with(view) {
                Glide.with(context).load(data.teamOneImg!!).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.dummy_team_icon).into(team_one_pic)
                Glide.with(context).load(data.teamTwoImg).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.dummy_team_icon).into(team_two_pic)

                team_one_name.text = data.teamOneName
                team_two_name.text = data.teamTwoName
                vsTeamsText.text = data.leagueTitle
                league_name.text = data.leagueSubTitle

                when (fragmentType) {
                    MatchFragmentType.FEED -> {
                        //ROHAN - Joined login. Waiting for XML
                        if (data.joined)
                            joined_group.visibility = View.VISIBLE
                        else
                            joined_group.visibility = View.GONE
                    }
                    MatchFragmentType.UPCOMING -> {
                        won_sub_text.text = activity.getString(R.string.contest_joined, data.contestsJoined)
                    }
                    MatchFragmentType.LIVE -> {
                        won_text.text = activity.getString(R.string.ongoing)
                        won_sub_text.text = activity.getString(R.string.contest_joined, data.contestsJoined)
                    }
                    MatchFragmentType.COMPLETED -> {
                        won_text.text = activity.getString(R.string.won_amount, Constants.removeExtraDecimal(data.amountWon))
                        won_sub_text.text = activity.getString(R.string.contest_joined, data.contestsJoined)
                        won_sub_text.setTextColor(resources.getColor(R.color.secondary_text_color))
                    }
                }

                itemView.setOnClickListener(View.OnClickListener {
                    if (!BuildConfig.is_gold) {
                        LockDialog(activity as Context).Builder().build().show()
                        return@OnClickListener
                    }

                    lateinit var bundle: Bundle
                    when (fragmentType) {
                        MatchFragmentType.FEED -> bundle = ContestFragment.getBundle(data.matchId, ContestType.CONTEST)
                        MatchFragmentType.UPCOMING -> bundle = ContestFragment.getBundle(data.matchId, ContestType.UPCOMING)
                        MatchFragmentType.LIVE -> bundle = ContestFragment.getBundle(data.matchId, ContestType.LIVE)
                        MatchFragmentType.COMPLETED -> bundle = ContestFragment.getBundle(data.matchId, ContestType.COMPLETED)
                    }
                    ContainerActivity.startActivity(context, ContainerActivity.FragmentTag.SPORTS_LEAGUES_CONTEST, bundle)
                })
            }
        }

        private fun setCounter(leagueModel: DataSportsLeague, timerView: TextView) {
            with(itemView) {
                val timeRemainingInMillis = maxOf((leagueModel.leagueEndTime - ServerTime.getServerTimeInS()) * 1000, 0)
                timerView.text = TimeUtils.convertMillisToString(timeRemainingInMillis);
                counter?.cancel()

                if (timeRemainingInMillis <= 0)
                    return

                counter = Counter.Builder(timeRemainingInMillis).setView(timerView)
                        .setOnFinishListener {
                            //Counter finished. Refresh data or Remove item)
                            fragmentInterface.refreshListData()
                        }
                        .build()

                counter?.start()
            }
        }

        fun detachFromWindow() {
            counter?.cancel()
        }

        fun attachToWindow() {
            val leagueModel = dataList[adapterPosition]
            when (fragmentType) {
                MatchFragmentType.FEED -> setCounter(leagueModel, view.time_left)
                MatchFragmentType.UPCOMING -> setCounter(leagueModel, view.won_text)
            }
        }
    }

    interface SportsLeagueAdapterInterface {
        fun refreshListData()
        fun getData(): MutableList<DataSportsLeague>
    }
}

//Model

class SportsLeagueModel {
    val mApiService: ApiService = ApiModule.getApiService();

    fun fetchLeagues(pageNo: Int, leagueList: MutableLiveData<ArrayList<DataSportsLeague>>,
                     promoList: MutableLiveData<ArrayList<DataPromo>>, loadingStatus: MutableLiveData<UI_STATE>,
                     disposable: CompositeDisposable) {
        loadingStatus.value = UI_STATE.PROGRESS

        mApiService.fetchLeagues(pageNo + 1).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : ResponseCodeObserver<DataSportsLeagueParent>(disposable) {
                    override fun onSuccess(value: DataSportsLeagueParent?) {
                        loadingStatus.postValue(UI_STATE.CONTENT)
                        leagueList.postValue(value?.matches)
                        value?.promos?.let { promoList.postValue(it) }
                    }

                    override fun onNetworkError(e: Throwable?) {
                        loadingStatus.postValue(UI_STATE.NETWORK_ERROR)
                    }

                    override fun onServerError(e: Throwable?, code: Int) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }
                })
    }

    fun fetchMyMatches(pageNo: Int, matchType: String, leagueList: MutableLiveData<ArrayList<DataSportsLeague>>,
                       loadingStatus: MutableLiveData<UI_STATE>, disposable: CompositeDisposable) {
        loadingStatus.value = UI_STATE.PROGRESS

        mApiService.fetchMyMatches(pageNo + 1, matchType).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : ResponseCodeObserver<DataSportsLeagueParent>(disposable) {
                    override fun onSuccess(value: DataSportsLeagueParent?) {
                        loadingStatus.postValue(UI_STATE.CONTENT)
                        leagueList.postValue(value?.matches)
                    }

                    override fun onNetworkError(e: Throwable?) {
                        loadingStatus.postValue(UI_STATE.NETWORK_ERROR)
                    }

                    override fun onServerError(e: Throwable?, code: Int) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }
                })
    }

    fun fetchMyContests(leagueId: Long, leagueModel: MutableLiveData<DataSportsLeague>,
                        contestList: MutableLiveData<ArrayList<DataContest>>, loadingStatus: MutableLiveData<UI_STATE>,
                        disposable: CompositeDisposable) {
        loadingStatus.value = UI_STATE.PROGRESS

        if (false) {
            var list = ArrayList<DataContest>()
            for (i in 0..5) {
                var data = DataContest()
                data.contestTitle = "TITLE"
                data.entryMoney = 15.8943f
                data.prizePoolMoney = 5000
                data.userTicketLimit = 8
                data.userTicketsCount = 5
                data.maxContestTickets = 432
                data.soldTickets = 111
                data.ticketId = 1
                data.isWinnerGenerated = false
                data.contestId = 1
                list.add(data)
            }

            contestList.value = list
            loadingStatus.value = UI_STATE.CONTENT
            return
        }

        mApiService.fetchMyContests(leagueId).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : ResponseCodeObserver<DataMyContestParent>(disposable) {
                    override fun onSuccess(value: DataMyContestParent?) {
                        loadingStatus.postValue(UI_STATE.CONTENT)
                        contestList.postValue(value?.contests)
                        leagueModel.postValue(value!!)
                    }

                    override fun onNetworkError(e: Throwable?) {
                        loadingStatus.postValue(UI_STATE.NETWORK_ERROR)
                    }

                    override fun onServerError(e: Throwable?, code: Int) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }
                })
    }

    fun fetchContests(pageNo: Int, leagueId: Long, leagueModel: MutableLiveData<DataSportsLeague>,
                      contestList: MutableLiveData<ArrayList<DataContest>>, loadingStatus: MutableLiveData<UI_STATE>,
                      disposable: CompositeDisposable) {
        loadingStatus.value = UI_STATE.PROGRESS

        mApiService.fetchContests(pageNo + 1, leagueId).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : ResponseCodeObserver<DataContestParent>(disposable) {
                    override fun onSuccess(value: DataContestParent?) {
                        loadingStatus.postValue(UI_STATE.CONTENT)

                        value?.contests?.let {
                            contestList.postValue(it)
                        }

                        value?.let {
                            leagueModel.postValue(it as DataSportsLeague)
                        }
                    }

                    override fun onNetworkError(e: Throwable?) {
                        loadingStatus.postValue(UI_STATE.NETWORK_ERROR)
                    }

                    override fun onServerError(e: Throwable?, code: Int) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }
                })
    }

    fun fetchPlayerStats(matchId: Long, playerStats: MutableLiveData<ArrayList<DataPlayerStats>>,
                         loadingStatus: MutableLiveData<UI_STATE>, disposable: CompositeDisposable) {
        loadingStatus.value = UI_STATE.PROGRESS

        mApiService.fetchPlayerStats(matchId).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : ResponseCodeObserver<DataPlayerStatsParent>(disposable) {
                    override fun onSuccess(value: DataPlayerStatsParent?) {
                        loadingStatus.postValue(UI_STATE.CONTENT)
                        playerStats.postValue(value?.playerStats)
                    }

                    override fun onNetworkError(e: Throwable?) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }

                    override fun onServerError(e: Throwable?, code: Int) {
                        loadingStatus.postValue(UI_STATE.SERVER_ERROR)
                    }

                })
    }
}

//Data

class DataSportsLeagueParent(
        @SerializedName("MATCHES")
        var matches: ArrayList<DataSportsLeague>,
        @SerializedName("PROMOS")
        var promos: ArrayList<DataPromo>,
        @SerializedName("NEXT_PAGE")
        var nextPage: Int
) : Serializable {}

open class DataSportsLeague() : Serializable {

    @SerializedName("TITLE")
    var leagueTitle: String = ""
    @SerializedName("SUB_TITLE")
    var leagueSubTitle: String = ""
    @SerializedName("END_TIME")
    var leagueEndTime: Long = 0
    @SerializedName("IMG_URL1")
    var teamOneImg: String = ""
    @SerializedName("IMG_URL2")
    var teamTwoImg: String = ""
    @SerializedName("TEAM_CODE_2")
    var teamTwoName: String = ""
    @SerializedName("TEAM_CODE_1")
    var teamOneName: String = ""
    @SerializedName("TOTAL_CONTESTS")
    var totalContests: Long = 0
    @SerializedName("CONTESTS_JOINED")
    var contestsJoined: Long = 0
    @SerializedName("AMOUNT_WON")
    var amountWon: Float = 0f
    @SerializedName("JOINED")
    var joined: Boolean = false
    @SerializedName("IS_PROMOTIONAL")
    var isPromotional: Boolean = false
    @SerializedName("PROMO_URL")
    var promoUrl: String = ""
    @SerializedName("MATCH_ID")
    var matchId: Long = -1
    @SerializedName("NUM_TICKETS")
    var userTicketCount: Long = 0
    @SerializedName("TOTAL_WINNING")
    var totalWinning: Float = 0f
    @SerializedName("SCORE_TEAM1")
    var scoreTeam1: String = ""
    @SerializedName("SCORE_TEAM2")
    var scoreTeam2: String = ""
    @SerializedName("OVERS_TEAM1")
    var oversTeam1: String = ""
    @SerializedName("OVERS_TEAM2")
    var oversTeam2: String = ""


    override fun toString(): String {
        return (matchId.toString() + " - " + leagueTitle + " - " + leagueSubTitle + " - " + leagueEndTime.toString()
                + " - " + teamOneName + " - " + teamTwoName + " - " + joined + " - " + isPromotional + " - " + promoUrl)
    }
}
