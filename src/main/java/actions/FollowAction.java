package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FavoriteView;
import actions.views.FollowView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.FavoriteService;
import services.FollowService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class FollowAction extends ActionBase {

    private FollowService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new FollowService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        long reportsCount = service.countAll();

        List<Integer> flagList = new ArrayList<>();
        for(ReportView rv : reports) {
        	Integer flag = (int)Math.floor(Math.random() * 2);
        	flagList.add(flag);
        }

        putRequestScope(AttributeConst.REPORTS, reports); //取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数
        putRequestScope(AttributeConst.REP_FLAG_LIST, flagList);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }
    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定済みの日報インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);

    }
    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {
//    	System.out.println("Follow#Create");

        //CSRF対策 tokenのチェック
        if (checkToken()) {

        	EmployeeService es = new EmployeeService();

            //セッションからログイン中の従業員情報を取得
            EmployeeView follow_v = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
            //リクエストパラメータからフォローする従業員インスタンスを取得
            EmployeeView follower_v = es.findOne(toNumber(getRequestParam(AttributeConst.FOL_FOLLOWER_ID)));

            //パラメータの値をもとにフォロー情報のインスタンスを作成する
            FollowView fv = new FollowView(
                    null,
                    follow_v, //ログインしている従業員を、日報作成者として登録する
                    follower_v,
                    null,
                    null);

            //フォロー情報登録
            List<String> errors = service.create(fv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, fv);//入力された日報情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //従業員詳細登録画面を再表示
                forward(ForwardConst.FW_EMP_SHOW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
//                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_SHOW);
//                ?action=Message&command=show&id=3
                response.sendRedirect(request.getContextPath() + "/?action=Employee&command=show&id=" + follower_v.getId());
            }
        }
    }
    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null) {
            //該当の日報データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            // FovoriteServiceインスタンスを生成
            FavoriteService fs = new FavoriteService();
            // ログインしている従業員が子の日報にいいねしているか判定
            Boolean favorite_flag = fs.getFavoriteCountByEmployeeANDReport(ev, rv);
            // この日報にいいねしている情報一覧を取得
            List<FavoriteView> favorites = fs.getFavoritesByReport(rv);
            // その日報にいいねしている人数を取得
            Integer favorites_count = favorites.size();


            // リクエストスコープにパラメータをセット
            putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.FAV_FLAG, favorite_flag);// すでにいいねしているかのフラグ
            putRequestScope(AttributeConst.FAV_FAVORITES, favorites);// いいね一覧
            putRequestScope(AttributeConst.FAV_COUNT, favorites_count);// いいね数

            //詳細画面を表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }
    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {
            //該当の日報データが存在しない、または
            //ログインしている従業員が日報の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv); //取得した日報データ

            //編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }

    }
    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に日報データを取得する
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));
            rv.setStart_time(request.getParameter("start_time"));
            rv.setEnd_time(request.getParameter("end_time"));

            //日報データを更新する
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_REP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }

    /**
	 * フォロー解除を行う
	 * @throws ServletException
	 * @throws IOException
	 */
	public void destroy() throws ServletException, IOException {

		//CSRF対策 tokenのチェック
		if (checkToken()) {

			EmployeeService es = new EmployeeService();
			//セッションからログイン中の従業員情報を取得
			EmployeeView follow_v = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

			//リクエストパラメータからフォローする従業員インスタンスを取得
            EmployeeView follower_v = es.findOne(toNumber(getRequestParam(AttributeConst.FOL_FOLLOWER_ID)));


			// FollowServiceインスタンスを使ってデータベースからフォロー情報を削除
			service.delete(follow_v, follower_v);

			//セッションに登録完了のフラッシュメッセージを設定
			putSessionScope(AttributeConst.FLUSH, MessageConst.FAV_DELETE.getMessage());

			// EmployeeActionのshowメソッドへのURLを構築
			String redirectUrl = request.getContextPath() + "/?action=" + ForwardConst.ACT_EMP.getValue() + "&command="
					+ ForwardConst.CMD_SHOW.getValue() + "&id=" + follower_v.getId();

			//URLへリダイレクト
			response.sendRedirect(redirectUrl);

		}
	}

}
