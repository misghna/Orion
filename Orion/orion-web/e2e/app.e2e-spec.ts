import { OrionWebPage } from './app.po';

describe('orion-web App', function() {
  let page: OrionWebPage;

  beforeEach(() => {
    page = new OrionWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
