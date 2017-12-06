public interface IViewer {
    public void viewTable();
    public void viewArticle(String articleNo);
    public void viewArticleRange(String firstArticle, String lastArticle);
    public void viewChapter(String chapterNo);
    public void viewChapterRange(String firstChapter, String lastChapter);
    public void viewSection(String sectionNo);
    public void viewSectionRange(String firstSection, String lastSection);
    public void viewPreAmbule();
}
