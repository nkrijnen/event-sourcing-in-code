package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import java.nio.file.Path

internal fun detectiveKantoor() = CatalogEntry(
    LegoSetNumber(10246),
    "Detectivekantoor",
    Path.of("lego-popular/10246-detectivekantoor.jpg"),
    popular = true
)

internal fun kampeerbus() = CatalogEntry(
    LegoSetNumber(10279),
    "Volkswagen T2 Kampeerbus",
    Path.of("lego-popular/10279-volkswagen-t2-kampeerbus.jpg"),
    popular = true
)

internal fun millenniumFalcon() = CatalogEntry(
    LegoSetNumber(75192),
    "Millennium Falcon",
    Path.of("lego-starwars/75192-millennium-falcon.jpg")
)

internal fun popularLegoSets() = listOf(
    fromFilename("lego-popular/76178-spiderman-daily-bugle.jpg"),
    fromFilename("lego-popular/10246-detectivekantoor.jpg"),
    fromFilename("lego-popular/10279-volkswagen-t2-kampeerbus.jpg"),
    fromFilename("lego-popular/10284-camp-nou-fc-barcelona.jpg"),
    fromFilename("lego-popular/10294-titanic.jpg"),
    fromFilename("lego-popular/21330-home-alone.jpg"),
    fromFilename("lego-popular/31120-middeleeuws-kasteel.jpg"),
    fromFilename("lego-popular/75978-diagon-alley-de-wegisweg.jpg"),
)


internal fun starWarsLegoSets() = listOf(
    fromFilename("lego-starwars/75159-death-star.jpg"),
    fromFilename("lego-starwars/75192-millennium-falcon.jpg"),
    fromFilename("lego-starwars/75244-tantive-iv.jpg"),
    fromFilename("lego-starwars/75252-imperial-star-destroyer.jpg"),
    fromFilename("lego-starwars/75275-a-wing-starfighter.jpg"),
    fromFilename("lego-starwars/75288-at-at.jpg"),
    fromFilename("lego-starwars/75290-mos-eisley-cantina.jpg"),
    fromFilename("lego-starwars/75308-r2-d2.jpg"),
    fromFilename("lego-starwars/75309-republic-gunship.jpg"),
    fromFilename("lego-starwars/75313-at-at.jpg"),
)