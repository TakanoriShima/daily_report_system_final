package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * お気に入りデータのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_FAV)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_FAV_FAVORITE_COUNT_BY_EMPLOYEE_AND_REPORT,
            query = JpaConst.Q_FAV_FAVORITE_COUNT_BY_EMPLOYEE_AND_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_FAV_GET_FAVORITE_BY_EMPLOYEE_AND_REPORT,
            query = JpaConst.Q_FAV_GET_FAVORITE_BY_EMPLOYEE_AND_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_FAV_GET_FAVORITES_BY_REPORT,
            query = JpaConst.Q_FAV_GET_FAVORITES_BY_REPORT_DEF),
    @NamedQuery(
            name = JpaConst.Q_FAV_GET_FAVORITES_BY_EMPLOYEE,
            query = JpaConst.Q_FAV_GET_FAVORITES_BY_EMPLOYEE_DEF)

})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Favorite {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.FAV_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * お気に入り登録した従業員
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FAV_COL_EMP, nullable = false)
    private Employee employee;

    /**
     * お気に入り登録された日報
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.FAV_COL_REP, nullable = false)
    private Report report;


    /**
     * 登録日時
     */
    @Column(name = JpaConst.FAV_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = JpaConst.FAV_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

}