package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Follow;
import models.Report;
import models.validators.FollowValidator;
import models.validators.ReportValidator;

/**
 * 日報テーブルの操作に関わる処理を行うクラス
 */
public class FollowService extends ServiceBase {

	/**
	 * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
	 * @param employee 従業員
	 * @param page ページ数
	 * @return 一覧画面に表示するデータのリスト
	 */
	public List<ReportView> getMinePerPage(EmployeeView employee, int page) {

		List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
				.setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();
		return ReportConverter.toViewList(reports);
	}

	/**
	 * 指定した従業員が作成した日報データの件数を取得し、返却する
	 * @param employee
	 * @return 日報データの件数
	 */
	public long countAllMine(EmployeeView employee) {

		long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
				.setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
				.getSingleResult();

		return count;
	}

	/**
	 * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
	 * @param page ページ数
	 * @return 一覧画面に表示するデータのリスト
	 */
	public List<ReportView> getAllPerPage(int page) {

		List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
				.setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
				.setMaxResults(JpaConst.ROW_PER_PAGE)
				.getResultList();
		return ReportConverter.toViewList(reports);
	}

	/**
	 * 日報テーブルのデータの件数を取得し、返却する
	 * @return データの件数
	 */
	public long countAll() {
		long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
				.getSingleResult();
		return reports_count;
	}

	/**
	 * idを条件に取得したデータをReportViewのインスタンスで返却する
	 * @param id
	 * @return 取得データのインスタンス
	 */
	public ReportView findOne(int id) {
		return ReportConverter.toView(findOneInternal(id));
	}

	/**
	 * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
	 * @param rv 日報の登録内容
	 * @return バリデーションで発生したエラーのリスト
	 */
	public List<String> create(FollowView fv) {
		List<String> errors = FollowValidator.validate(fv);
		if (errors.size() == 0) {
			LocalDateTime ldt = LocalDateTime.now();
			fv.setCreatedAt(ldt);
			fv.setUpdatedAt(ldt);
			createInternal(fv);
		}

		//バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
		return errors;
	}

	/**
	 * 画面から入力された日報の登録内容を元に、日報データを更新する
	 * @param rv 日報の更新内容
	 * @return バリデーションで発生したエラーのリスト
	 */
	public List<String> update(ReportView rv) {

		//バリデーションを行う
		List<String> errors = ReportValidator.validate(rv);

		if (errors.size() == 0) {

			//更新日時を現在時刻に設定
			LocalDateTime ldt = LocalDateTime.now();
			rv.setUpdatedAt(ldt);

			updateInternal(rv);
		}

		//バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
		return errors;
	}

	/**
	 * idを条件にデータを1件取得する
	 * @param id
	 * @return 取得データのインスタンス
	 */
	private Report findOneInternal(int id) {
		return em.find(Report.class, id);
	}

	/**
	 * 日報データを1件登録する
	 * @param rv 日報データ
	 */
	private void createInternal(FollowView fv) {

		em.getTransaction().begin();
		em.persist(FollowConverter.toModel(fv));
		em.getTransaction().commit();

	}

	/**
	 * 日報データを更新する
	 * @param rv 日報データ
	 */
	private void updateInternal(ReportView rv) {

		em.getTransaction().begin();
		Report r = findOneInternal(rv.getId());
		ReportConverter.copyViewToModel(r, rv);
		em.getTransaction().commit();

	}

	/**
	 * idを条件にデータを1件取得する
	 * @param follow_v EmployeeViewインスタンス
	 * @param follower_v EmployeeViewインスタンス
	 * @return void
	 */
	public void delete(EmployeeView follow_v, EmployeeView follower_v) {
		// モデルインスタンスへ変換
		Employee follow = EmployeeConverter.toModel(follow_v);
		Employee follower = EmployeeConverter.toModel(follower_v);
		// 削除するFollowインスタンスを取得
		Follow f = em.createNamedQuery("getFollowByFollowANDFollower", Follow.class).setParameter("follow", follow).setParameter("follower", follower).getSingleResult();
		// 削除処理
		em.getTransaction().begin();
		em.remove(f);
		em.getTransaction().commit();

	}

	// ログイン従業員が、注目している従業員にすでにいいねしているか判定
	public Boolean getFollowCountByFollowANDFollower(EmployeeView follow_v, EmployeeView follower_v) {
		Employee follow = EmployeeConverter.toModel(follow_v);
		Employee follower = EmployeeConverter.toModel(follower_v);
		long count = em.createNamedQuery("getFollowCountByFollowANDFollower", Long.class).setParameter("follow", follow)
				.setParameter("follower", follower).getSingleResult();
		// そんな情報がデータベースに存在しなければ
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

}
