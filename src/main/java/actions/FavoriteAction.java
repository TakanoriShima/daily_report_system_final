package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FavoriteView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import services.FavoriteService;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class FavoriteAction extends ActionBase {

	private FavoriteService service;

	/**
	 * メソッドを実行する
	 */
	@Override
	public void process() throws ServletException, IOException {

		service = new FavoriteService();

		//メソッドを実行
		invoke();

		service.close();
	}

	/**
	 * いいね登録を行う
	 * @throws ServletException
	 * @throws IOException
	 */
	public void create() throws ServletException, IOException {

		//CSRF対策 tokenのチェック
		if (checkToken()) {

			//セッションからログイン中の従業員情報を取得
			EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

			// ReportServiceインスタンスを生成
			ReportService rs = new ReportService();

			// idを条件に日報データを取得する
			ReportView rv = rs.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

			//パラメータの値をもとにお気に入り情報のインスタンスを作成する
			FavoriteView fv = new FavoriteView(
					null,
					ev, //ログインしている従業員をいいねした従業員として登録する
					rv, //いいねされた日報を登録する
					null,
					null);

			//お気に入り情報登録
			service.create(fv);

			//セッションに登録完了のフラッシュメッセージを設定
			putSessionScope(AttributeConst.FLUSH, MessageConst.FAV_REGISTERED.getMessage());

			// ReportActionのshowメソッドへのURLを構築
			String redirectUrl = request.getContextPath() + "/?action=" + ForwardConst.ACT_REP.getValue() + "&command="
					+ ForwardConst.CMD_SHOW.getValue() + "&id=" + getRequestParam(AttributeConst.REP_ID);

			//URLへリダイレクト
			response.sendRedirect(redirectUrl);

		}
	}

	/**
	 * いいね削除を行う
	 * @throws ServletException
	 * @throws IOException
	 */
	public void destroy() throws ServletException, IOException {

		//CSRF対策 tokenのチェック
		if (checkToken()) {
			//セッションからログイン中の従業員情報を取得
			EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

			// ReportServiceインスタンスを取得
			ReportService rs = new ReportService();

			//idを条件に日報データを取得する
			ReportView rv = rs.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

			// FavoriteServiceインスタンスを使ってデータベースからいいね情報を削除
			service.delete(ev, rv);

			//セッションに登録完了のフラッシュメッセージを設定
			putSessionScope(AttributeConst.FLUSH, MessageConst.FAV_DELETE.getMessage());

			// ReportActionのshowメソッドへのURLを構築
			String redirectUrl = request.getContextPath() + "/?action=" + ForwardConst.ACT_REP.getValue() + "&command="
					+ ForwardConst.CMD_SHOW.getValue() + "&id=" + getRequestParam(AttributeConst.REP_ID);

			//URLへリダイレクト
			response.sendRedirect(redirectUrl);

		}
	}
}
