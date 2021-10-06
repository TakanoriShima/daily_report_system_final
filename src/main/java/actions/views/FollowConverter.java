package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;

/**
 * フォローデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class FollowConverter {

	/**
	 * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
	 * @param fv FollowViewのインスタンス
	 * @return Followのインスタンス
	 */
	public static Follow toModel(FollowView fv) {
		return new Follow(
				fv.getId(),
				EmployeeConverter.toModel(fv.getFollow()),
				EmployeeConverter.toModel(fv.getFollower()),
				fv.getCreatedAt(),
				fv.getUpdatedAt());
	}

	/**
	 * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
	 * @param f Followのインスタンス
	 * @return FollowViewのインスタンス
	 */
	public static FollowView toView(Follow f) {

		if (f == null) {
			return null;
		}

		return new FollowView(
				f.getId(),
				EmployeeConverter.toView(f.getFollow()),
				EmployeeConverter.toView(f.getFollower()),
				f.getCreatedAt(),
				f.getUpdatedAt());
	}

	/**
	 * DTOモデルのリストからViewモデルのリストを作成する
	 * @param list DTOモデルのリスト
	 * @return Viewモデルのリスト
	 */
	public static List<FollowView> toViewList(List<Follow> list) {
		List<FollowView> fvs = new ArrayList<>();

		for (Follow f : list) {
			fvs.add(toView(f));
		}

		return fvs;
	}

	/**
	 * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
	 * @param f DTOモデル(コピー先)
	 * @param fv Viewモデル(コピー元)
	 */
	public static void copyViewToModel(Follow f, FollowView fv) {
		f.setId(fv.getId());
		f.setFollow(EmployeeConverter.toModel(fv.getFollow()));
		f.setFollower(EmployeeConverter.toModel(fv.getFollower()));
		f.setCreatedAt(fv.getCreatedAt());
		f.setUpdatedAt(fv.getUpdatedAt());
	}

	/**
	 * DTOモデルの全フィールドの内容をViewモデルのフィールドにコピーする
	 * @param f DTOモデル(コピー元)
	 * @param fv Viewモデル(コピー先)
	 */
	public static void copyModelToView(Follow f, FollowView fv) {
		fv.setId(f.getId());
		fv.setFollow(EmployeeConverter.toView(f.getFollow()));
		fv.setFollower(EmployeeConverter.toView(f.getFollower()));
		fv.setCreatedAt(f.getCreatedAt());
		fv.setUpdatedAt(f.getUpdatedAt());
	}

}
