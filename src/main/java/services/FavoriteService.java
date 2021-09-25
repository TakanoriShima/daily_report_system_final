package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FavoriteConverter;
import actions.views.FavoriteView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Favorite;
import models.Report;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class FavoriteService extends ServiceBase {

	/**
	 * お気に入りテーブルに情報を登録する
	 * @param fv FavoriteViewインスタンス
	 * @return void
	 */
	public void create(FavoriteView fv) {

		// 現在日時を取得
		LocalDateTime ldt = LocalDateTime.now();
		// セッターで値をセット
		fv.setCreatedAt(ldt);
		fv.setUpdatedAt(ldt);
		// データベースに保存
		createInternal(fv);

	}

	/**
	 * idを条件にデータを1件取得する
	 * @param ev EmployeeViewインスタンス
	 * @param rv ReportViewインスタンス
	 * @return void
	 */
	public void delete(EmployeeView ev, ReportView rv) {
		// モデルインスタンスへ変換
		Employee e = EmployeeConverter.toModel(ev);
		Report r = ReportConverter.toModel(rv);
		// 削除するFavoriteインスタンスを取得
		Favorite f = em.createNamedQuery("getFavoriteByEmployeeANDReport", Favorite.class).setParameter("employee", e)
				.setParameter("report", r).getSingleResult();
		// 削除処理
		em.getTransaction().begin();
		em.remove(f);
		em.getTransaction().commit();

	}

	/**
	 * お気に入りデータを1件登録する
	 * @param fv FavoriteViewインスタンス
	 */
	private void createInternal(FavoriteView fv) {

		em.getTransaction().begin();
		em.persist(FavoriteConverter.toModel(fv));
		em.getTransaction().commit();

	}

	// 注目している従業員が、注目している日報にすでにいいねしているか判定
	public Boolean getFavoriteCountByEmployeeANDReport(EmployeeView ev, ReportView rv) {
		Employee e = EmployeeConverter.toModel(ev);
		Report r = ReportConverter.toModel(rv);
		long count = em.createNamedQuery("getFavoriteCountByEmployeeANDReport", Long.class).setParameter("employee", e)
				.setParameter("report", r).getSingleResult();
		// そんな情報がデータベースに存在しなければ
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	// 注目している日報に対するいいねリスト取得
	public List<FavoriteView> getFavoritesByReport(ReportView rv) {
		Report r = ReportConverter.toModel(rv);
		List<Favorite> favorites = em.createNamedQuery("getFavoritesByReport", Favorite.class).setParameter("report", r)
				.getResultList();
		return FavoriteConverter.toViewList(favorites);
	}


	 /**
     * 指定した従業員のお気に入りした一覧を取得
     * @param ev EmployeeViewインスタンス
     * @return 一覧画面に表示するお気に入りデータのリスト
     */
    public List<FavoriteView> getFavoritesByEmployee(EmployeeView ev) {

    	Employee e = EmployeeConverter.toModel(ev);

        List<Favorite> favorites_by_employee = em.createNamedQuery(JpaConst.Q_FAV_GET_FAVORITES_BY_EMPLOYEE, Favorite.class)
        		.setParameter("employee", e)
//                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
//                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return FavoriteConverter.toViewList(favorites_by_employee);
    }

}
