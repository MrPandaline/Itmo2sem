package laba5.server.db;

import laba5.common.genetics.Gene;
import laba5.common.genetics.Genome;
import laba5.common.model.Dragon;
import laba5.server.logic.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class DragonGenomeDBProcessor {
    private final DBManager dbManager;

    public DragonGenomeDBProcessor(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public long insert(Long dragonId, Genome genome) throws SQLException {
        LinkedList<Dragon> dragons = new LinkedList<>();
        String sql = "INSERT INTO dragonGenomes(id_dragon, wingSizeGene, eyeGene, " +
                "hornGene, patternGene) VALUES(?,?,?,?,?) " +
                "ON CONFLICT (id_dragon) DO UPDATE SET " +
                "wingSizeGene = EXCLUDED.wingSizeGene, " +
                "eyeGene = EXCLUDED.eyeGene, " +
                "hornGene = EXCLUDED.hornGene, " +
                "patternGene = EXCLUDED. patternGene";

        try (PreparedStatement stmt = dbManager.getPreparedStatementRGK(sql)){
            stmt.setLong(1, dragonId);
            stmt.setString(2, genome.getWingSizeGene().toString());
            stmt.setString(3, genome.getEyeGene().toString());
            stmt.setString(4, genome.getHornGene().toString());
            stmt.setString(5, genome.getPatternGene().toString());
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Вставка генома не выполнена! Таблица не изменена.");
            }
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getLong(1);
                } else {
                    throw new SQLException("Вставка генома не выполнена! ID не получен.");
                }
            }
        }
    }
    public Genome select(Long dragonId) throws SQLException {
        String sql = "SELECT * FROM dragonGenomes WHERE id_dragon = ?";
        try (PreparedStatement prst = dbManager.getPreparedStatement(sql)){
            prst.setLong(1, dragonId);
            ResultSet rs = prst.executeQuery();

            if (rs.next()) {
                return new Genome(
                        Gene.fromString(rs.getString("eyeGene")),
                        Gene.fromString(rs.getString("wingSizeGene")),
                        Gene.fromString(rs.getString("hornGene")),
                        Gene.fromString(rs.getString("patternGene"))
                );
            }

        }
        return null;
    }

    public LinkedList<Genome> select() throws SQLException {
        LinkedList<Genome> genomes = new LinkedList<>();
        String sql = "SELECT * FROM dragonGenomes";
        try (ResultSet rs = dbManager.getStatement().executeQuery(sql)){
            if (rs.next()) {
                Genome curGenome = new Genome(
                        Gene.fromString(rs.getString("eyeGene")),
                        Gene.fromString(rs.getString("wingSizeGene")),
                        Gene.fromString(rs.getString("hornGene")),
                        Gene.fromString(rs.getString("patternGene"))
                );
                genomes.add(curGenome);
            }
        }
        return genomes;
    }
}
