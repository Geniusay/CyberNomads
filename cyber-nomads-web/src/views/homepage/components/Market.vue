<template>
  <section class="section market" aria-label="market update" data-section>
          <div class="container">
            <div class="title-wrapper">
              <h2 class="h2 section-title">Market Update</h2>

              <a href="#" class="btn-link">See All Coins</a>
            </div>

            <div class="market-tab">
              <ul class="tab-nav">
                <li>
                  <button class="tab-btn active">View All</button>
                </li>

                <li>
                  <button class="tab-btn">Metaverse</button>
                </li>

                <li>
                  <button class="tab-btn">Entertainment</button>
                </li>

                <li>
                  <button class="tab-btn">Energy</button>
                </li>

                <li>
                  <button class="tab-btn">NFT</button>
                </li>

                <li>
                  <button class="tab-btn">Gaming</button>
                </li>

                <li>
                  <button class="tab-btn">Music</button>
                </li>
              </ul>

              <table class="market-table">
                <thead class="table-head">
                  <tr class="table-row table-title">
                    <th class="table-heading"></th>

                    <th class="table-heading" scope="col">#</th>

                    <th class="table-heading" scope="col">Name</th>

                    <th class="table-heading" scope="col">Last Price</th>

                    <th class="table-heading" scope="col">24h %</th>

                    <th class="table-heading" scope="col">Market Cap</th>

                    <th class="table-heading" scope="col">Last 7 Days</th>

                    <th class="table-heading"></th>
                  </tr>
                </thead>

                <tbody class="table-body">
                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">1</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-1.svg"
                          width="20"
                          height="20"
                          alt="Bitcoin logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Bitcoin <span class="span">BTC</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update green">+1.45%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-1.svg"
                        width="100"
                        height="40"
                        alt="profit chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">2</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-2.svg"
                          width="20"
                          height="20"
                          alt="Ethereum logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Ethereum <span class="span">ETH</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update red">-5.12%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-2.svg"
                        width="100"
                        height="40"
                        alt="loss chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">3</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-3.svg"
                          width="20"
                          height="20"
                          alt="Tether logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Tether <span class="span">USDT/USD</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update green">+1.45%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-1.svg"
                        width="100"
                        height="40"
                        alt="profit chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">4</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-4.svg"
                          width="20"
                          height="20"
                          alt="BNB logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >BNB <span class="span">BNB/USD</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update red">-3.75%%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-2.svg"
                        width="100"
                        height="40"
                        alt="loss chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">5</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-5.svg"
                          width="20"
                          height="20"
                          alt="Solana logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Solana <span class="span">SOL</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update green">+1.45%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-1.svg"
                        width="100"
                        height="40"
                        alt="profit chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">6</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-6.svg"
                          width="20"
                          height="20"
                          alt="XRP logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >XRP <span class="span">XRP</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update red">-2.22%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-2.svg"
                        width="100"
                        height="40"
                        alt="loss chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">7</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-7.svg"
                          width="20"
                          height="20"
                          alt="Cardano logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Cardano <span class="span">ADA</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update green">+0.8%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-1.svg"
                        width="100"
                        height="40"
                        alt="profit chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>

                  <tr class="table-row">
                    <td class="table-data">
                      <button
                        class="add-to-fav"
                        aria-label="Add to favourite"
                        data-add-to-fav
                      >
                        <ion-icon
                          name="star-outline"
                          aria-hidden="true"
                          class="icon-outline"
                        ></ion-icon>
                        <ion-icon
                          name="star"
                          aria-hidden="true"
                          class="icon-fill"
                        ></ion-icon>
                      </button>
                    </td>

                    <th class="table-data rank" scope="row">8</th>

                    <td class="table-data">
                      <div class="wrapper">
                        <img
                          src="@/assets/images/coin-8.svg"
                          width="20"
                          height="20"
                          alt="Avalanche logo"
                          class="img"
                        />

                        <h3>
                          <a href="#" class="coin-name"
                            >Avalanche <span class="span">AVAX</span></a
                          >
                        </h3>
                      </div>
                    </td>

                    <td class="table-data last-price">$56,623.54</td>

                    <td class="table-data last-update green">+1.41%</td>

                    <td class="table-data market-cap">$880,423,640,582</td>

                    <td class="table-data">
                      <img
                        src="@/assets/images/chart-1.svg"
                        width="100"
                        height="40"
                        alt="profit chart"
                        class="chart"
                      />
                    </td>

                    <td class="table-data">
                      <button class="btn btn-outline">Trade</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>

</template>

<style scoped>
@import url("@/styles/view/homepage/_market.scss");
</style>
