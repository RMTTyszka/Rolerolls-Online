package com.loh.campaign;

import com.loh.campaign.dtos.AddPlayerAndHeroToCampaignInput;
import com.loh.campaign.dtos.CampaignInvitation;
import com.loh.campaign.dtos.PlayerInvitationsOutput;
import com.loh.context.Player;
import com.loh.context.PlayerRepository;
import com.loh.creatures.heroes.Hero;
import com.loh.creatures.heroes.HeroRepository;
import com.loh.shared.BaseCrudController;
import com.loh.shared.BaseCrudResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.loh.authentication.LohUserDetails.currentUserId;

@CrossOrigin
@Controller    // This means that this class is a Controller
@RequestMapping(path="/campaigns",  produces = "application/json; charset=UTF-8")
public class CampaignController extends BaseCrudController<Campaign> {

	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	InvitedPlayerRepository invitedPlayerRepository;
	@Autowired
	HeroRepository heroRepository;

	@Autowired
	CampaignRepository repository;

	public CampaignController(CampaignRepository repository) {
		super(repository);
	}

	@Override
	public Campaign getnew() {
		return new Campaign();
	}

	@Override
	@Transactional
	public @ResponseBody
	BaseCrudResponse<Campaign> delete(@RequestParam UUID id) {
		Campaign campaign = repository.findById(id).get();
		if(campaign.isMaster()) {
			invitedPlayerRepository.deleteAllByCampaignId(id);
			return super.delete(id);
		}
		return new BaseCrudResponse<Campaign>(false, "You are not the master of that campaign");
	}
	public @ResponseBody BaseCrudResponse<Campaign> add(@RequestBody Campaign campaign) {
		UUID userId = currentUserId();
		campaign.setMasterId(userId);
		campaign = repository.save(campaign);
		return new BaseCrudResponse<Campaign>(true, "Campaign successfuly created", campaign);
	}
	@GetMapping(path="/player/invite/get")
	public  @ResponseBody
    PlayerInvitationsOutput getPlayerInvitations() {
		UUID playerId = currentUserId();
		List<InvitedPlayer> invitations = invitedPlayerRepository.findAllByPlayerIdAndStatus(playerId, InvitationStatus.Sent);
		List<UUID> campaignIds = invitations.stream().map(i -> i.getCampaignId()).collect(Collectors.toList());
		List<Campaign> campaigns = repository.findAllByIdIn(campaignIds);
		return new PlayerInvitationsOutput(campaigns);
	}
	@GetMapping(path="/{campaignId}/invite/get")
	public  @ResponseBody
	List<CampaignInvitation> getCampaignInvitations(@PathVariable UUID campaignId) {
		List<InvitedPlayer> invitations = invitedPlayerRepository.findAllByCampaignId(campaignId);
		List<UUID> playerIds = invitations.stream().map(i -> i.getPlayerId()).collect(Collectors.toList());
        List<CampaignInvitation> players = StreamSupport.stream(playerRepository.findAllById(playerIds).spliterator(), false).map(p -> new CampaignInvitation(p, invitations.stream().filter(i -> i.getPlayerId().equals(p.getId())).findFirst().get().getStatus())).collect(Collectors.toList());
		return players;
	}
	@PostMapping(path="/player/invite")
	@ResponseStatus(HttpStatus.OK)
	public void invitePlayer(@RequestBody AddPlayerAndHeroToCampaignInput input) throws NoPermissionException {
		Campaign campaign = repository.findById(input.campaignId).get();
		if (campaign.isMaster()) {
			InvitedPlayer invitedPlayer = new InvitedPlayer(input.campaignId, input.playerId);
			invitedPlayerRepository.save(invitedPlayer);
			return;
		} else {
			throw new NoPermissionException("You are not the master of that campaign");
		}

	}	
	@DeleteMapping(path="/player/invite/delete/{campaignId}/{playerId}")
	@ResponseStatus(HttpStatus.OK)
    @Transactional
	public void deleteInvitation(@PathVariable UUID campaignId, @PathVariable UUID playerId) {
		Campaign campaign = repository.findById(campaignId).get();
		if (campaign.isMaster()) {
			invitedPlayerRepository.deleteByCampaignIdAndPlayerId(campaignId, playerId);
		}
	}
	@PostMapping(path="/player/invite/deny/{campaignId}")
	@ResponseStatus(HttpStatus.OK)
	public void denyInvitation(@PathVariable UUID campaignId) {
		UUID playerId = currentUserId();
		InvitedPlayer invitedPlayer = invitedPlayerRepository.findByCampaignIdAndPlayerId(campaignId, playerId);
		invitedPlayer.setStatus(InvitationStatus.Denied);
		invitedPlayerRepository.save(invitedPlayer);
	}
    @PostMapping(path="/player/add/{campaignId}")
    @ResponseStatus(HttpStatus.OK)
    public void addPlayer(@PathVariable UUID campaignId) {
        UUID playerId = currentUserId();
        Campaign campaign = repository.findById(campaignId).get();
        Player player = playerRepository.findById(playerId).get();
        campaign.addPlayer(player);
        repository.save(campaign);
        InvitedPlayer invitedPlayer = invitedPlayerRepository.findByCampaignIdAndPlayerId(campaignId, playerId);
        invitedPlayer.setStatus(InvitationStatus.Accepted);
        invitedPlayerRepository.save(invitedPlayer);

    }
	@DeleteMapping(path="/player/remove/{campaignId}/{playerId}")
	@ResponseStatus(HttpStatus.OK)
	public void removePlayer(@PathVariable UUID campaignId, @PathVariable UUID playerId) throws NoPermissionException {
		Campaign campaign = repository.findById(campaignId).get();
		if (campaign.isMaster()) {
			campaign.removePlayer(playerId);
			repository.save(campaign);
			InvitedPlayer invitedPlayer = invitedPlayerRepository.findByCampaignIdAndPlayerId(campaignId, playerId);
			invitedPlayerRepository.delete(invitedPlayer);
		} else {
			throw new NoPermissionException("You are not the master of this campaign");
		}

	}
	@GetMapping(path="/heroes/select")
	public @ResponseBody
	Iterable<Hero> getHeroesForSelect(@RequestParam(required = false) String filter, @RequestParam UUID campaignId, @RequestParam int skipCount, @RequestParam int maxResultCount, Principal userDetails) {
		Campaign campaign = repository.findById(campaignId).get();
		List<UUID> playerIds = campaign.getPlayers().stream().map(p -> p.getId()).collect(Collectors.toList());
		List<Hero> heroesForSelect = heroRepository.findAll(fromPlayers(playerIds));
		return heroesForSelect;
	}
	@GetMapping(path="/player/select")
	public @ResponseBody
	Iterable<Player> getPlayersForInvite(@RequestParam(required = false) String filter, @RequestParam UUID campaignId, @RequestParam int skipCount, @RequestParam int maxResultCount) {
		List<InvitedPlayer> invitedPlayers = invitedPlayerRepository.findAllByCampaignId(campaignId);
		List<UUID> alreadyInvitedPlayerIds = invitedPlayers.stream().map(p -> p.getPlayerId()).collect(Collectors.toList());
		Pageable paged = PageRequest.of(skipCount, maxResultCount);
		List<Player> playersForSelect = playerRepository.findAll(ignoringPlayers(alreadyInvitedPlayerIds), paged).getContent();
		return playersForSelect;
	}

	private Specification<Hero> fromPlayers(List<UUID> playerIds) {
		if (playerIds == null || playerIds.isEmpty()) {
			return (hero, cq, cb) -> cb.isNotNull(hero);
		}
		return (hero, cq, cb) -> hero.get("ownerId").in(playerIds);
	}
	private Specification<Player> ignoringPlayers(List<UUID> playerIds) {
		if (playerIds == null || playerIds.isEmpty()) {
			return (player, cq, cb) -> cb.isNotNull(player);
		}
		return (player, cq, cb) -> player.get("id").in(playerIds).not();
	}

}
