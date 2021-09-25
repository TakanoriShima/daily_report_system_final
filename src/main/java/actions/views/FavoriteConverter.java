package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Favorite;

/**
 * お気に入りデータのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class FavoriteConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param fv FavoriteViewのインスタンス
     * @return Favoriteのインスタンス
     */
    public static Favorite toModel(FavoriteView fv) {
        return new Favorite(
        		fv.getId(),
                EmployeeConverter.toModel(fv.getEmployee()),
                ReportConverter.toModel(fv.getReport()),
                fv.getCreatedAt(),
                fv.getUpdatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param f Favoriteのインスタンス
     * @return FavoriteViewのインスタンス
     */
    public static FavoriteView toView(Favorite f) {

        if (f == null) {
            return null;
        }

        return new FavoriteView(
                f.getId(),
                EmployeeConverter.toView(f.getEmployee()),
                ReportConverter.toView(f.getReport()),
                f.getCreatedAt(),
                f.getUpdatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<FavoriteView> toViewList(List<Favorite> list) {
        List<FavoriteView> fvs = new ArrayList<>();

        for (Favorite f : list) {
            fvs.add(toView(f));
        }

        return fvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param r DTOモデル(コピー先)
     * @param rv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Favorite f, FavoriteView fv) {
        f.setId(fv.getId());
        f.setEmployee(EmployeeConverter.toModel(fv.getEmployee()));
        f.setReport(ReportConverter.toModel(fv.getReport()));
        f.setCreatedAt(fv.getCreatedAt());
        f.setUpdatedAt(fv.getUpdatedAt());

    }

    /**
     * DTOモデルの全フィールドの内容をViewモデルのフィールドにコピーする
     * @param r DTOモデル(コピー元)
     * @param rv Viewモデル(コピー先)
     */
    public static void copyModelToView(Favorite f, FavoriteView fv) {
        fv.setId(f.getId());
        fv.setEmployee(EmployeeConverter.toView(f.getEmployee()));
        fv.setReport(ReportConverter.toView(f.getReport()));
        fv.setCreatedAt(f.getCreatedAt());
        fv.setUpdatedAt(f.getUpdatedAt());
    }

}
