package nl.vu.cs.softwaredesign.service;

import nl.vu.cs.softwaredesign.domain.Appliance;
import nl.vu.cs.softwaredesign.domain.House;
import nl.vu.cs.softwaredesign.domain.Room;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PDFExporter {

    private final Statistics statistics;

    public PDFExporter() {
        this.statistics = new Statistics();
    }

    public void generateReport(String fileName) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);

                addReportTitle(contentStream);
                addTimeStats(contentStream);
                addTopAppliances(contentStream);

                contentStream.endText();
            }
            document.save(fileName);
        }
    }

    private void addReportTitle(PDPageContentStream contentStream) throws IOException {
        contentStream.showText("Carbon Footprint Report");
        contentStream.newLineAtOffset(0, -20);
    }

    private void addTimeStats(PDPageContentStream contentStream) throws IOException {
        Map<Integer, Integer> bestTimeMap = statistics.calculateOptimalTime();
        if (!bestTimeMap.isEmpty()) {
            Map.Entry<Integer, Integer> bestTimeEntry = bestTimeMap.entrySet().iterator().next();
            contentStream.showText("Best time to use appliances: " + bestTimeEntry.getKey() + ":00 with carbon intensity of " + bestTimeEntry.getValue() + " gCO2");
        } else {
            contentStream.showText("Best time to use appliances: Data unavailable");
        }
        contentStream.newLineAtOffset(0, -20);

        Map<Integer, Integer> worstTimeMap = statistics.calculateWorstTime();
        if (!worstTimeMap.isEmpty()) {
            Map.Entry<Integer, Integer> worstTimeEntry = worstTimeMap.entrySet().iterator().next();
            contentStream.showText("Worst time to use appliances: " + worstTimeEntry.getKey() + ":00 with carbon intensity of " + worstTimeEntry.getValue() + " gCO2");
        } else {
            contentStream.showText("Worst time to use appliances: Data unavailable");
        }
        contentStream.newLineAtOffset(0, -20);
    }

    private void addTopAppliances(PDPageContentStream contentStream) throws IOException {
        List<Appliance> topAppliances = statistics.getTopContributors();
        if (topAppliances == null || topAppliances.isEmpty()) {
            contentStream.showText("No appliances found.");
            contentStream.newLineAtOffset(0, -20);
        } else {
            int limit = Math.min(3, topAppliances.size());
            for (int i = 0; i < limit; i++) {
                Appliance appliance = topAppliances.get(i);
                Room room = getRoomByAppliance(appliance);
                String roomName = room != null ? room.getName() : "Kitchen";

                contentStream.showText((i + 1) + ". " + appliance.getModel() + " in room " + roomName);
                contentStream.newLineAtOffset(0, -15);

                double totalContribution = statistics.getContributionToTotal(roomName, appliance.getModel());
                contentStream.showText("  Contribution to Total: " + totalContribution + "%");
                contentStream.newLineAtOffset(0, -15);

                double roomContribution = statistics.getContributionToRoom(roomName, appliance.getModel());
                contentStream.showText("  Contribution to " + roomName + ": " + roomContribution + "%");
                contentStream.newLineAtOffset(0, -20);
            }
        }
    }

    private Room getRoomByAppliance(Appliance appliance) {
        for (Room room : House.getInstance()) {
            for (Appliance roomAppliance : room.getApplianceList()) {
                if (roomAppliance.getModel().equals(appliance.getModel())) {
                    return room;
                }
            }
        }
        return null;
    }
}
